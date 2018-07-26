package org.delphy.tokenheroserver.service;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import lombok.extern.slf4j.Slf4j;
import org.delphy.tokenheroserver.common.EnumError;
import org.delphy.tokenheroserver.entity.Activity;
import org.delphy.tokenheroserver.entity.Forecast;
import org.delphy.tokenheroserver.entity.User;
import org.delphy.tokenheroserver.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mutouji
 */
@Slf4j
@Service
public class SocketIoService {
    private static SocketIOServer server;
    private Map<SocketIOClient, UserVo> usersMap = new ConcurrentHashMap<>();

    private OracleService oracleService;
    private IndexService indexService;
    private ForecastService forecastService;
    private UserService userService;

    public SocketIoService(@Autowired OracleService oracleService,
                           @Autowired ForecastService forecastService,
                           @Autowired UserService userService,
                           @Autowired IndexService indexService) {
        this.oracleService = oracleService;
        this.forecastService = forecastService;
        this.indexService = indexService;
        this.userService = userService;
    }

    public void startServer() {
        Configuration config = new Configuration();
        config.setHostname("0.0.0.0");
        config.setPort(26668);

        server = new SocketIOServer(config);

        server.addConnectListener((SocketIOClient client) -> {
            String sa = client.getRemoteAddress().toString();
            String clientIp = sa.substring(1, sa.indexOf(":"));

            client.getHandshakeData().getHttpHeaders().set("access-control-allow-origin", "*");
            String userToken = client.getHandshakeData().getSingleUrlParam("userToken");
            if (userToken != null && !userToken.isEmpty()) {
                UserVo userVo = indexService.getToken(userToken);
                if (userVo != null) {
                    usersMap.put(client, userVo);
                    log.info("ws:" + clientIp + ": welcome " + userVo);
                }
            }
        });

        server.addDisconnectListener((SocketIOClient client) -> {
            String sa = client.getRemoteAddress().toString();
            String clientIp = sa.substring(1, sa.indexOf(":"));

            usersMap.remove(client);

            log.info("ws:" + clientIp + " bye ");
        });

        server.addEventListener("xx", String.class, (SocketIOClient client, String data, AckRequest ackSender) -> {
            log.info(data);
        });

        server.start();
    }

    public void sendAllNews(Activity activity) {
        OracleNewsVo oracleNewsVo = this.oracleService.getNews(activity.getOracleId());
        if (oracleNewsVo.getCode() == EnumError.SUCCESS.getCode()) {
            ActivityNewsVo newsVo = new ActivityNewsVo();
            newsVo.setNews(oracleNewsVo.getData());
            server.getBroadcastOperations().sendEvent("news", newsVo);
        }
    }

    private void sendAllPopMessage(PopMessageSocketVo messageVo) {
        server.getBroadcastOperations().sendEvent("pop", messageVo);
    }
    public void popMessage(UserVo userVo, String message) {
        User user = userService.getUser(userVo.getPhone());
        if (user != null) {
            PopMessageSocketVo popMessageSocketVo = new PopMessageSocketVo();
            popMessageSocketVo.setAvatar(user.getAvatar());
            popMessageSocketVo.setMessage(message);
            popMessageSocketVo.setName(user.getName());
            popMessageSocketVo.setPhone(user.getPhone());
            Collection<SocketIOClient> clients = server.getAllClients();
            System.out.println("client size = "+ clients.size() + ". usersMap" + usersMap.size());
            System.out.println(usersMap.toString());
            sendAllPopMessage(popMessageSocketVo);
        }
    }
    public void sendAllResult(Activity activity) {
        for (SocketIOClient client : usersMap.keySet()) {
            UserVo userVo = usersMap.get(client);
            Forecast forecast = forecastService.getForecast(activity, userVo.getId());
            ForecastResultsVo forecastResultsVo = new ForecastResultsVo(forecast, activity);
            client.sendEvent("result", forecastResultsVo);
        }
    }

    public void sendAllNewActivity() {
        for (SocketIOClient client : usersMap.keySet()) {
            client.sendEvent("activitychange");
        }
    }
}

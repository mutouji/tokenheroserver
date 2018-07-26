package org.delphy.tokenheroserver;

import org.delphy.tokenheroserver.service.SocketIoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author mutouji
 * @PropertySource("file:${tokenheroserver_config_path:D:/tokenhero/tokenheroserver/src/main/resources}/application.properties")
 */
@EnableScheduling
@EnableWebMvc
@SpringBootApplication
public class TokenheroserverApplication implements CommandLineRunner {
    private SocketIoService socketIoService;
    public TokenheroserverApplication(@Autowired SocketIoService socketIoService) {
        this.socketIoService = socketIoService;
    }

    public static void main(String[] args) {
        SpringApplication.run(TokenheroserverApplication.class, args);
    }

    @Override
    public void run(String... args) {
        runSocket();
    }
    public void runSocket(){
        try {
            System.out.println("runSocket");
            this.socketIoService.startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

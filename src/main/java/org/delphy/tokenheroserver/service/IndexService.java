package org.delphy.tokenheroserver.service;

import org.delphy.tokenheroserver.common.Constant;
import org.delphy.tokenheroserver.entity.User;
import org.delphy.tokenheroserver.pojo.UserVo;
import org.delphy.tokenheroserver.util.RedisUtil;
import org.delphy.tokenheroserver.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author mutouji
 */
@Service
public class IndexService {
    private RedisUtil redisUtil;
    private NotifyService notifyService;
    private UserService userService;

    public IndexService( @Autowired NotifyService notifyService,
                        @Autowired RedisUtil redisUtil,
                        @Autowired UserService userService) {
        this.notifyService = notifyService;
        this.userService = userService;
        this.redisUtil = redisUtil;
    }

    public boolean sendVerifyCode(String phone, String verifyCode) {
        return notifyService.sendVerifyCode(phone, verifyCode);
    }

    public String getVerifyCode(String phone) {
        return redisUtil.getObject(Constant.CACHE_VERIFYCODE + phone);
    }

    public String generateVerifyCode(String phone) {
        String code = TimeUtil.generateVerifyCode();
        redisUtil.setObjectMinite(Constant.CACHE_VERIFYCODE + phone, code, Constant.TIME_VERIFYCODE);
        return code;
    }

    public UserVo getToken(String token) {
        return redisUtil.getObject(Constant.CACHE_TOKEN + token);
    }

    public UserVo generateToken(String phone, String id) {
        String token = genUniqueId();
        UserVo userVo = new UserVo(token, phone, id);
        redisUtil.setObjectMinite(Constant.CACHE_TOKEN + token, userVo, Constant.TIME_TOKE);
        return userVo;
    }

    public User getUser(String phone) {
        return userService.getUser(phone);
    }

    public User generateUser(String phone) {
        return userService.generateUser(phone);
    }

    private String genUniqueId() {
        return UUID.randomUUID().toString();
    }
}

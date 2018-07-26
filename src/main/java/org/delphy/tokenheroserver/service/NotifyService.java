package org.delphy.tokenheroserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mutouji
 */
@Service
public class NotifyService {
    private SmsService smsService;

    public NotifyService(@Autowired SmsService smsService) {
        this.smsService = smsService;
    }

    boolean sendVerifyCode(String phone, String verifyCode) {
        return smsService.sendVerifyCode(phone, verifyCode);
    }
}

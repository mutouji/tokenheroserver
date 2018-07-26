package org.delphy.tokenheroserver.service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.delphy.tokenheroserver.common.EnumError;
import org.delphy.tokenheroserver.exception.NormalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author mutouji
 */
@Slf4j
@Service
public class SmsService {
    @Value("${luosimao.url}")
    private String url;
    @Value("${luosimao.appKey}")
    private String appKey;
    @Value("${luosimao.message}")
    private String message;
    @Value("${luosimao.apiKey}")
    private String apiKey;

    private RestTemplate restTemplate;

    public SmsService(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    boolean sendVerifyCode(String phone, String verifyCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/x-www-form-urlencoded");
        String auth = "Basic " + Base64.encodeBase64String(("api:" + appKey).getBytes());
        headers.add("Authorization", auth);

        MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
        String message = String.format("您的验证码是%s,５分钟内有效。若非本人操作请忽略此消息。【delphy】", verifyCode);
        params.add("mobile", phone);
        params.add("message", message);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<Map<String,Object>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {});
        Map<String, Object> rt = response.getBody();
        /*
         * status=200, msg={error=0, msg=ok}
         */
        if (response.getStatusCode() == HttpStatus.OK) {
            int error = (Integer) rt.get("error");
            if (error == 0) {
                return true;
            }
        }

        String error = "status=" + response.getStatusCode() + ", msg=" + rt;
        log.error(error);
        throw new NormalException(EnumError.SMS_ERROR.getCode(), error);
    }
}

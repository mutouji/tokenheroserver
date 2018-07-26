package org.delphy.tokenheroserver.controller;

import io.swagger.annotations.*;
import lombok.Data;
import org.delphy.tokenheroserver.annotation.RequestLimit;
import org.delphy.tokenheroserver.common.EnumError;
import org.delphy.tokenheroserver.common.RestResult;
import org.delphy.tokenheroserver.entity.User;
import org.delphy.tokenheroserver.pojo.UserVo;
import org.delphy.tokenheroserver.service.IndexService;
import org.delphy.tokenheroserver.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mutouji
 */
@Api(value="IndexController",description = "登陆页",tags={"登陆"})
@RestController
public class IndexController {
    private IndexService indexService;

    public IndexController(@Autowired IndexService indexService) {
        this.indexService = indexService;
    }

    @ApiOperation(value="获取验证码")
    @PostMapping("/verifycode")
    @RequestLimit(count=1)
    public RestResult<String> verifyCode(@RequestBody VerifyCodeParams verifyCodeParams, HttpServletRequest request) {
        /*
         * 算法
         * 0.TODO: 检查电话号码合法
         * 1.检查用户手机验证码是否还在，在的话，直接返回
         * 2.如果不在，生成verifycode，保存到redis里,并返还给用户
         */
        RequestUtil.logRequestIpAndUrl(request);
        if (!verifyCodeParams.isFieldValid()) {
            return new RestResult<>(EnumError.PHONE_NUMBER);
        }
        String verifyCode = this.indexService.generateVerifyCode(verifyCodeParams.getPhone());
        if (!this.indexService.sendVerifyCode(verifyCodeParams.getPhone(), verifyCode)) {
            return new RestResult<>(EnumError.VERIFY_SEND);
        }
        return new RestResult<>(EnumError.SUCCESS);
    }

    @ApiOperation(value="用户登陆")
    @PostMapping("/login")
    @RequestLimit(count=10)
    public RestResult<String> login(@RequestBody SignInParams signInParams, HttpServletRequest request) {
        RequestUtil.logRequestIpAndUrl(request);
        if (!signInParams.isFieldValid()) {
            return new RestResult<>(EnumError.REQUEST_PARAM);
        }

        String code = this.indexService.getVerifyCode(signInParams.getPhone());
        if (code == null) {
            return new RestResult<>(EnumError.VERIFY_CODE_EXPIRE);
        }
        if (!code.equals(signInParams.getVerifyCode())) {
            return new RestResult<>(EnumError.VERIFY_CODE_WRONG);
        }
        // 如果用户不存在，则生成一个新的，否则用老用户
        User user = indexService.getUser(signInParams.getPhone());
        if (user == null) {
            user = indexService.generateUser(signInParams.getPhone());
        }
        // 为用户产生token
        UserVo userVo = indexService.generateToken(user.getPhone(), user.getId());

        return new RestResult<>(EnumError.SUCCESS, userVo.getToken());
    }

    @ApiModel(description = "登陆时，需要传递过来的实体")
    @Data
    static class SignInParams {
        @ApiModelProperty(value="verifyCode", dataType = "string", required = true, example = "123456")
        private String verifyCode = null;
        @ApiModelProperty(value="phone", dataType = "string", required = true, example = "13436665547")
        private String phone = null;

        boolean isFieldValid() {
            return !(phone == null || verifyCode == null || phone.isEmpty() || verifyCode.isEmpty());
        }
    }

    @ApiModel(description = "发送验证码时，需要的参数")
    @Data
    static class VerifyCodeParams {
        @ApiModelProperty(value="phone", dataType = "string", required = true, example = "13436665547")
        private String phone = null;
        boolean isFieldValid() {
            return !(phone == null || phone.isEmpty());
        }
    }
}

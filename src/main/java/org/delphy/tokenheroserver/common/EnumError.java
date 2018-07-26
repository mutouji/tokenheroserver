package org.delphy.tokenheroserver.common;

/**
 * @author mutouji
 */
public enum EnumError {
    /**
     *
     */
    SUCCESS(0, "success"),
    PHONE_NUMBER(2001, "手机号格式非法"),
    VERIFY_SEND(2002, "验证码发送服务异常"),
    REQUEST_PARAM(2003, "非法的请求参数"),
    VERIFY_CODE_EXPIRE(2004, "验证码过期"),
    VERIFY_CODE_WRONG(2004, "验证码错误"),
    INCORRECT_TOKEN(2104, "登录过期，请重新登录"),
    SMS_ERROR(2101, "发送短信失败"),
    ACTIVITY_NULL(2200, "活动不存在"),
    ACTIVITY_MODE_ERROR(2201, "活动模式错误,0小程序，1H5"),
    ACTIVITY_INVALID_STATUS(2202, "活动不处于可参与状态"),
    ACTIVITY_INVALID_TIME(2203, "已超过活动有效参与时间"),
    ACTIVITY_UNSETTLEMENTED(2206, "活动尚未清算"),
    ORACLE_CODE(2204, "oracle错误"),
    ACTIVITY_OUTOF_PRECAST_TIMES(2205, "超出可用预测次数"),
    ACTIVITY_NOT_PROCESSING(2206, "只能在活动进行中发弹幕"),
    MESSAGE_SENSITIVE(2207, "消息中有敏感信息"),
    WITHDRAW_BAD_PARAM(2210, "提币申请参数错误"),
    WITHDRAW_LT_MIN(2211, "最小提现额度2DPY"),

    INSUFFICIENT_DPY(2301, "dpy余额不足"),
    BADADRESS(2303, "地址格式错误"),
    USER_ERROR(2302, "用户不存在");

    EnumError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

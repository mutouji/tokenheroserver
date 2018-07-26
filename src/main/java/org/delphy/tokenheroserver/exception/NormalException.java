package org.delphy.tokenheroserver.exception;

/**
 * @author mutouji
 */
public class NormalException extends RuntimeException{
    private int code;
    private String msg;

    public NormalException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

package org.delphy.tokenheroserver.common;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.delphy.tokenheroserver.util.TimeUtil;

import java.io.Serializable;

/**
 * @author mutouji
 */
@Slf4j
@Data
public class RestResult<T> implements Serializable {
    private int code;
    private String msg;
    private long timestamp = TimeUtil.getCurrentSeconds();
    private T data;

    public RestResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        if (code != 0) {
            log.error("code:"+code + ",msg:" + msg + ",data:" + data);
        }
    }

    public RestResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
        if (code != 0) {
            log.error("code:"+code + ",msg:" + msg);
        }
    }

    public RestResult(EnumError enumError, T data) {
        this.code = enumError.getCode();
        this.msg = enumError.getMsg();
        this.data = data;
        if (code != 0) {
            log.error("code:"+code + ",msg:" + msg + ",data:" + data);
        }
    }
    public RestResult(EnumError enumError) {
        this.code = enumError.getCode();
        this.msg = enumError.getMsg();
        this.data = null;
        if (code != 0) {
            log.error("code:"+code + ",msg:" + msg);
        }
    }
}

package org.delphy.tokenheroserver.pojo;

import lombok.Data;

/**
 * @author mutouji
 */
@Data
public class SmsVo {
    private String err;
    private String msg;
    private String srv;

    private String phone;
}

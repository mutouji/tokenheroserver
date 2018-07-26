package org.delphy.tokenheroserver.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author mutouji
 */
@Data
public class UserVo implements Serializable {
    private String token;
    private String phone;
    private String id;

    public UserVo(String token, String phone, String id) {
        this.token = token;
        this.phone = phone;
        this.id = id;
    }
}

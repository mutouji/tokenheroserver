package org.delphy.tokenheroserver.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author mutouji
 */
@Data
@Document(collection = "adminuser")
public class AdminUser implements Serializable {
    @Id
    private String id;
    private String account;
    /**
     * md5 加密
     */
    private String pwd;
    private String url;
    /**
     * 1
     */
    private Long power;
    /**
     * admin
     */
    private String name;
    /**
     * admin
     */
    private String duties;
    private String phone;
    private String jobNum;

    private Long create;
}

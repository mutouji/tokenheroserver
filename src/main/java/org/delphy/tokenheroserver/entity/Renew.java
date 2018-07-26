package org.delphy.tokenheroserver.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * @author mutouji
 */
@Data
@Document(collection = "Renew")
public class Renew implements Serializable {
    /**
     * 15234306268076227 微秒+4位
     */
    @Id
    private String id;
    /**
     * 15234269096816341
     */
    private String userId;
    /**
     * 6 = invitedIds.length
     */
    private Long renew;
    /**
     * user id s
     * 1 523 430 630 590 758
     * 1 523 877 145 979 3609
     * 1 523 877 301 235 6624
     * 1 523 877 332 106 25
     * 1 526 983 898 092 6640
     * 1 526 984 152 422 9557
     */
    private List<String> invitedIds;
    private Long create;
}

package org.delphy.tokenheroserver.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author mutouji
 */
@Data
@Document(collection = "subscribe")
public class Subscribe implements Serializable {
    /**
     * 1523432456622120
     */
    @Id
    private String id;
    /**
     * 15234324253659807
     */
    private String userId;
    /**
     * 15234271896699455
     */
    private String activityId;
    /**
     * 0没删
     */
    private Long delete;
}

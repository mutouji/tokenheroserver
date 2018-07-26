package org.delphy.tokenheroserver.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * price used in Forecast
 * @author mutouji
 */
@Data
public class Price implements Serializable {
    private Double price;
    private Double rate;
    private Long time;

    public Price(Double price, Double rate, Long time) {
        this.price = price;
        this.rate = rate;
        this.time = time;
    }
}

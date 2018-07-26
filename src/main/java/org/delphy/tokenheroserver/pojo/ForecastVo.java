package org.delphy.tokenheroserver.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.delphy.tokenheroserver.entity.Activity;
import org.delphy.tokenheroserver.entity.Forecast;
import org.delphy.tokenheroserver.entity.Price;

import java.util.List;

/**
 * @author mutouji
 */
@ApiModel(description = "进行预测")
@Data
public class ForecastVo {
    @ApiModelProperty(value="价格", required = true, example = "88.88")
    private Double price;

    /**
     * prices :last 在0号位置
     * start <=
     * @param activity activity
     * @param forecast forecast
     * @param time time
     */
    public void initForecast(Activity activity, Forecast forecast, long time) {
        long usedTime = time - activity.getStart() + 1;
        long holdTime = activity.getHold() * 60;
        long percent = (holdTime - usedTime) * 100 / holdTime + 1;
        double ratio = percent / 100.0f;
        Price p = new Price(price, ratio, time);
        List<Price> ps = forecast.getPrices();
        ps.add(0, p);

        forecast.setFreeNum(forecast.getFreeNum() - 1);
        forecast.setLast(p);
        forecast.setPrices(ps);
        forecast.setRewardRatio(ratio);
    }
}

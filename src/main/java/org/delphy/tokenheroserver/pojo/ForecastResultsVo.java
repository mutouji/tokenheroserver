package org.delphy.tokenheroserver.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.delphy.tokenheroserver.entity.Activity;
import org.delphy.tokenheroserver.entity.Forecast;

/**
 * @author mutouji
 */
@ApiModel(description = "预测记录")
@Data
public class ForecastResultsVo {
    @ApiModelProperty(value="forecastId", required = true, example = "11123456")
    private String forecastId;
    @ApiModelProperty(value="activityId", required = true, example = "22222222")
    private String activityId;
    @ApiModelProperty(value="标题", required = true, example = "预测btc 2018年7月8日15点的价格")
    private String title;
    @ApiModelProperty(value="获得dpy的奖励数额", required = true, example = "15")
    private Double reward;
    @ApiModelProperty(value="我的预测价格", required = true, example = "6888.88")
    private Double myPrice;
    @ApiModelProperty(value="市场的结果", required = true, example = "6887.88")
    private Double result;
    @ApiModelProperty(value="获胜的价格浮动比例", required = true, example = "0.001")
    private Double priceRatio;
    @ApiModelProperty(value="获胜时，收益的百分比", required = true, example = "0.88")
    private Double benefitRatio;
    @ApiModelProperty(value="最小值", required = true, example = "6886")
    private Double min;
    @ApiModelProperty(value="最大值", required = true, example = "6990")
    private Double max;
    @ApiModelProperty(value="是否赢了", required = true, example = "true")
    private Boolean isWin;

    public ForecastResultsVo(Forecast forecast, Activity activity) {
        this.activityId = activity.getId();
        this.title = activity.getTitle();
        this.result = activity.getResult();
        this.priceRatio = activity.getRewardRatio();
        this.min = activity.getMinWin();
        this.max = activity.getMaxWin();

        if (forecast != null) {
            this.forecastId = forecast.getId();
            this.reward = forecast.getReward();
            this.myPrice = forecast.getLast().getPrice();
            this.benefitRatio = forecast.getRewardRatio();
            this.isWin = forecast.isWin(min, max);
        } else {
            this.forecastId = null;
            this.reward = 0.0;
            this.myPrice = 0.0;
            this.benefitRatio = 0.0;
            this.isWin = false;
        }
    }
}

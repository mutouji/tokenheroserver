package org.delphy.tokenheroserver.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.delphy.tokenheroserver.entity.Activity;
import org.delphy.tokenheroserver.entity.Forecast;

/**
 * @author mutouji
 */
@ApiModel(description = "预测结果")
@Data
public class ResultVo {
    @ApiModelProperty(value="获得奖金", required = true, example = "0.16")
    private Double reword;
    @ApiModelProperty(value="奖池(DPY)", required = true, example = "200")
    private Long pond;
    @ApiModelProperty(value="获奖人数", required = true, example = "197")
    private Long winnerCount;
    @ApiModelProperty(value="币种", required = true, example = "ETH/USDT")
    private String market;
    @ApiModelProperty(value="交易所", required = true, example = "币安")
    private String datasource;
    @ApiModelProperty(value="结算时间", required = true, example = "1530067800")
    private Long settlementTime;
    @ApiModelProperty(value="清盘价格", required = true, example = "516.1")
    private Double finalPrice;
    @ApiModelProperty(value="我的价格", required = true, example = "516.3")
    private Double myPrice;
    @ApiModelProperty(value="收益比例", required = true, example = "0.16")
    private Double benefitRatio;
    @ApiModelProperty(value="获奖价格浮动比例", required = true, example = "0.001")
    private Double priceRatio;
    @ApiModelProperty(value = "参加活动了吗", required = true, example = "true")
    private Boolean isJoin;
    @ApiModelProperty(value = "赢了吗", required = true, example = "true")
    private Boolean isWin;

    public void buildResultVo(Activity activity, Forecast forecast, Long count) {
        if (activity == null) {
            return;
        }
        Double benefitRatio = 0.0;
        Double myPrice = 0.0;
        Double reword = 0.0;
        Boolean isJoin = false;
        Boolean isWin = false;
        Long settlementTime = activity.getGetOracleTime();
        if (forecast != null) {
            benefitRatio = forecast.getRewardRatio();
            if (forecast.getLast() != null) {
                myPrice = forecast.getLast().getPrice();
                if (count > 0 && forecast.isWin(activity.getMinWin(), activity.getMaxWin())) {
                    isWin = true;
                }
            }
            reword = forecast.getReward();
            settlementTime = forecast.getRewardTime();
            isJoin = true;
        }
        this.setBenefitRatio(benefitRatio);
        this.setDatasource(activity.getDatasource());
        this.setFinalPrice(activity.getResult());
        this.setMarket(activity.getPair() + "/" + activity.getBase());
        this.setMyPrice(myPrice);
        this.setPond(activity.getPond());
        this.setPriceRatio(activity.getRewardRatio());
        this.setReword(reword);
        this.setSettlementTime(settlementTime);
        this.setWinnerCount(count);
        this.setIsJoin(isJoin);
        this.setIsWin(isWin);
    }
}

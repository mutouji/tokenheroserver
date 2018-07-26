package org.delphy.tokenheroserver.entity;

import lombok.Data;
import org.delphy.tokenheroserver.common.Constant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author mutouji
 */
@Data
@Document(collection = "activity")
public class Activity implements Serializable {
    /**
     * 15236027302838709 微秒 + 4位
     */
    @Id
    private String id;
    /**
     * 预测EOS在20点10分的收盘价是多少？允许误差范围0.1%
     */
    private String title;
    /**
     * 开始时间1,523,620,500 单位秒
     */
    private Long start;
    /**
     * 奖金池400 单位dpy
     */
    private Long pond;
    /**
     * 持续时间10 单位分钟
     */
    private Long hold;
    /**
     * 锁定时间 5 单位分钟
     */
    private Long lockTime;
    /**
     * 源币种  EOS
     */
    private String pair;
    /**
     * 参照币种  ETH
     */
    private String base;
    /**
     * 市场结束时间 start + (hold  + lockTime) * 60 + settlement time (2 * 60)
     */
    private Long end;
    /**
     * 数据源 币安
     */
    private String datasource;
    /**
     * 中奖范围0.001
     */
    private Double rewardRatio;
    /**
     * 状态: 未开始1，进行中2，锁定中3，清算中4，已结束5
     */
    private Long status;
    /**
     * 是否清算 否0, 是1
     */
    private Long isSettlement;
    /**
     * 延迟？
     */
    private Long delayed;
    /**
     *  结果 0.01805
     */
    private Double result;
    /**
     * 获取oracle 结果的时间 秒 1,523,621,460
     */
    private Long getOracleTime;
    /**
     * oracle id  102
     */
    private Long oracleId;
    /**
     * oracle type
     * 类型 币安1 篮球2 足球3
     */
    private Long type;
    /**
     * 0 微信小程序活动， 1 h5 活动
     */
    private Long mode;
    /**
     * 本条记录的创建时间1,523,602,730
     */
    private Long create;
    /**
     * 是否删除 0 没删除 1 已经删除
     */
    private Long delete;

    public Long getCurrentStatus(long time) {
        long minitue = 60;
        long t = time - start;
        if (t < 0) {
            return Constant.ACTIVITY_INIT;
        }
        if (t <= hold * minitue) {
            return Constant.ACTIVITY_PROCESSING;
        }
        if (t <= (hold + lockTime) * minitue ) {
            return Constant.ACTIVITY_LOCKED;
        }
        return Constant.ACTIVITY_CLEARING;
    }

    public double getMinWin() {
        return (1.0 - getRewardRatio()) * result;
    }

    public double getMaxWin() {
        return (1.0 + getRewardRatio()) * result;
    }

    public double getRewardRatio() {
        return rewardRatio != null ? rewardRatio : 0.001;
    }
}

//{
//        "ret": true,
//        "data": {
//        "list": [
//        {
//        "id": "15282855095881481",
//        "title": "预测ETH在20点02分的收盘价是多少？命中范围为0.1%",
//        "start": "2018-06-06 19:46",
//        "pond": 600,
//        "hold": 10,
//        "lockTime": 5,
//        "pair": "ETH",
//        "base": "USDT",
//        "end": 1528286580,
//        "datasource": "币安",
//        "status": 4,
//        "isSettlement": 0,
//        "delayed": 0,
//        "result": 0,
//        "getOrcaleTime": "2018-06-06 20:02:00",
//        "oracleId": 33,
//        "type": 1,
//        "create": 1528285509,
//        "delete": 0
//        },
//        {
//        "id": "15268978812449207",
//        "title": "预测EOS在19点45分的收盘价是多少？命中范围为0.1%",
//        "start": "2018-05-21 19:30",
//        "pond": 100,
//        "hold": 10,
//        "lockTime": 5,
//        "pair": "EOS",
//        "base": "ETH",
//        "end": 1526903220,
//        "datasource": "币安",
//        "status": 5,
//        "isSettlement": 1,
//        "delayed": 0,
//        "result": 0.019026,
//        "getOrcaleTime": "2018-05-21 19:46:00",
//        "oracleId": 6,
//        "type": 1,
//        "create": 1526897881,
//        "delete": 0
//        }
//        ],
//        "count": 65
//        },
//        "msg": "success"
//        }
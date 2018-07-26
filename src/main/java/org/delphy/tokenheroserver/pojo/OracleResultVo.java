package org.delphy.tokenheroserver.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * {"code":0,"data":{"scenarioIndex":0,"resultValues":[531.22],"manualChecked":false,"disputeNum":0,"disputePeriodEnded":true}}
 * @author mutouji
 */
@ApiModel(description = "获取oracle结果")
@Data
public class OracleResultVo {
    private Long scenarioIndex;
    private List<String> resultValues;
    private Boolean manualChecked;
    private Long disputeNum;
    private Boolean disputePeriodEnded;
}

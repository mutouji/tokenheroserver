package org.delphy.tokenheroserver.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author mutouji
 */
@ApiModel(description = "获取oracle最新状态")
@Data
public class OracleNewsVo {
    @ApiModelProperty(value="oracle id")
    private Long code;
    @ApiModelProperty(value="news[0]当前的价格")
    private List<String> data;
}

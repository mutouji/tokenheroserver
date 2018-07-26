package org.delphy.tokenheroserver.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author mutouji
 */
@ApiModel(description = "最新消息")
@Data
public class ActivityNewsVo {
    @ApiModelProperty(value="最新消息（价格）", required = true, example = "[88.88]")
    private List<String> news;
}

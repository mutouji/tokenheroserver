package org.delphy.tokenheroserver.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mutouji
 */
@ApiModel(description = "弹幕消息")
@Data
public class PopMessageVo {
    @ApiModelProperty(value="内容", required = true, example = "王五预测价格是:100")
    private String message;
}

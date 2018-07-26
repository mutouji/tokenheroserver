package org.delphy.tokenheroserver.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mutouji
 */
@ApiModel(description = "弹幕消息")
@Data
public class PopMessageSocketVo {
    @ApiModelProperty(value="内容", required = true, example = "王五预测价格是:100")
    private String message;
    @ApiModelProperty(value="头像链接", required = true)
    private String avatar;
    @ApiModelProperty(value="发言人名称", required = true, example = "卖火柴的小姑娘")
    private String name;
    @ApiModelProperty(value="发言人手机上", required = true, example = "13436665547")
    private String phone;
}

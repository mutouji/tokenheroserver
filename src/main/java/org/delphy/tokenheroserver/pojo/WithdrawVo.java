package org.delphy.tokenheroserver.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author mutouji
 */
@ApiModel(description = "提币申请")
@Data
public class WithdrawVo {
    @ApiModelProperty(value="以太坊地址", required = true, example = "0x12345678901234567890")
    private String address;
    @ApiModelProperty(value="DPY数量", required = true, example = "88.88")
    private Double amount;

    @ApiIgnore
    public boolean isValid() {
        return address != null && amount > 0.0;
    }
}

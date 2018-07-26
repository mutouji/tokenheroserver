package org.delphy.tokenheroserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.delphy.tokenheroserver.annotation.RequestLimit;
import org.delphy.tokenheroserver.common.EnumError;
import org.delphy.tokenheroserver.common.RestResult;
import org.delphy.tokenheroserver.entity.Withdraw;
import org.delphy.tokenheroserver.pojo.UserVo;
import org.delphy.tokenheroserver.pojo.WithdrawVo;
import org.delphy.tokenheroserver.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author mutouji
 */
@Api(value="WithdrawController",description = "提币申请",tags={"提币"})
@RestController
public class WithdrawController {
    private WithdrawService withdrawService;
    public WithdrawController(@Autowired WithdrawService withdrawService) {
        this.withdrawService = withdrawService;
    }

    @ApiOperation("提币申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid", value = "token", paramType = "header", dataType = "string", required = true)
    })
    @PostMapping("/withdraw")
    @RequestLimit(count = 1)
    public RestResult<String> withdraw(@ApiIgnore UserVo userVo, @RequestBody WithdrawVo withdrawVo) {
        if (!withdrawVo.isValid()) {
            return new RestResult<>(EnumError.WITHDRAW_BAD_PARAM);
        }
        EnumError enumError = withdrawService.withdraw(userVo, withdrawVo);
        return new RestResult<>(enumError);
    }

    @ApiOperation("获取用户的提币记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid", value = "token", paramType = "header", dataType = "string", required = true),
            @ApiImplicitParam(name = "page", value = "分页索引", paramType = "query", dataType = "int", defaultValue = "0"),
            @ApiImplicitParam(name = "size", value = "分页大小", paramType = "query", dataType = "int", defaultValue = "2")
    })
    @GetMapping("/withdraws")
    public RestResult<List<Withdraw>> getUserWithdraw(@ApiIgnore UserVo userVo, Integer page, Integer size) {
        List<Withdraw> withdraws = withdrawService.getUserWithdraw(userVo, page, size);
        return new RestResult<>(EnumError.SUCCESS, withdraws);
    }

    @ApiOperation("获取用户的提币记录总数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid", value = "token", paramType = "header", dataType = "string", required = true)
    })
    @GetMapping("/withdrawscount")
    public RestResult<Long> getUserWithdrawCount(@ApiIgnore UserVo userVo) {
        Long withdrawsCount = withdrawService.getUserWithdrawCount(userVo);
        return new RestResult<>(EnumError.SUCCESS, withdrawsCount);
    }
}

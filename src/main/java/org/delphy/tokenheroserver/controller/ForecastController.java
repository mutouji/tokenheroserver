package org.delphy.tokenheroserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.delphy.tokenheroserver.annotation.RequestLimit;
import org.delphy.tokenheroserver.common.Constant;
import org.delphy.tokenheroserver.common.EnumError;
import org.delphy.tokenheroserver.common.RestResult;
import org.delphy.tokenheroserver.entity.Activity;
import org.delphy.tokenheroserver.entity.Forecast;
import org.delphy.tokenheroserver.pojo.*;
import org.delphy.tokenheroserver.service.ActivityService;
import org.delphy.tokenheroserver.service.ForecastService;
import org.delphy.tokenheroserver.service.SocketIoService;
import org.delphy.tokenheroserver.util.RequestUtil;
import org.delphy.tokenheroserver.util.WordFilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author mutouji
 */
@Api(value = "ForecastController", description = "预测记录接口", tags={"预测记录"})
@RestController
public class ForecastController {
    private ForecastService forecastService;
    private ActivityService activityService;
    private SocketIoService socketIoService;
    private WordFilterUtil wordFilterUtil;

    public ForecastController(@Autowired ForecastService forecastService,
                              @Autowired ActivityService activityService,
                              @Autowired SocketIoService socketIoService,
                              @Autowired WordFilterUtil wordFilterUtil) {
        this.forecastService = forecastService;
        this.wordFilterUtil = wordFilterUtil;
        this.activityService = activityService;
        this.socketIoService = socketIoService;
    }

    @ApiOperation(value="获取竞猜记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid", value = "token", paramType = "header", dataType = "string", required = true),
            @ApiImplicitParam(name = "page", value = "分页索引", paramType = "query", dataType = "int", defaultValue = "0"),
            @ApiImplicitParam(name = "size", value = "分页大小", paramType = "query", dataType = "int", defaultValue = "2")
    })
    @GetMapping("/forecasts")
    public RestResult<List<ForecastResultsVo>> getForecasts(@ApiIgnore UserVo userVo, Integer page, Integer size) {
        List<ForecastResultsVo> forecasts;
        forecasts = forecastService.getForecasts(userVo.getId(), page, size);
        return new RestResult<>(EnumError.SUCCESS, forecasts);
    }

    @ApiOperation("提交预测")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid", value = "token", paramType = "header", dataType = "string", required = true),
            @ApiImplicitParam(name = "mode", value = "0 小程序，1 h5活动", paramType = "query", dataType = "int", defaultValue = "1")
    })
    @PostMapping("/forecasts")
    public RestResult<Forecast> setForecast(@ApiIgnore UserVo userVo, @RequestBody ForecastVo forecastVo, Long mode) {
        RestResult<Forecast> result = forecastService.trySetForecasts(userVo.getId(), forecastVo, mode);
        if (result.getCode() == 0) {
            socketIoService.popMessage(userVo, forecastVo.getPrice().toString());
        }
        return result;
    }

    @ApiOperation("获取最近一次的预测结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid", value = "token", paramType = "header", dataType = "string", required = true),
            @ApiImplicitParam(name = "mode", value = "0 小程序，1 h5活动", paramType = "path", dataType = "int", defaultValue = "1")
    })
    @GetMapping("/lastforecast/{mode}")
    public RestResult<Forecast> getLastForecast(@ApiIgnore UserVo userVo, @PathVariable Long mode) {
        Forecast forecast = forecastService.getLastForecast(userVo.getId(), mode);

        return new RestResult<>(EnumError.SUCCESS, forecast);
    }

    @ApiOperation("发送一条弹幕消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid", value = "token", paramType = "header", dataType = "string", required = true),
            @ApiImplicitParam(name = "mode", value = "0 小程序，1 h5活动", paramType = "path", dataType = "int", defaultValue = "1")
    })
    @PostMapping("/popmessage/{mode}")
    public RestResult<String> popMessage(@ApiIgnore UserVo userVo, @RequestBody PopMessageVo popMessageVo, @PathVariable Long mode) {
        Activity activity = activityService.getMainActivityFromCache(mode);
        if (activity == null) {
            return new RestResult<>(EnumError.ACTIVITY_NULL);
        }
        if (activity.getStatus().equals(Constant.ACTIVITY_INIT) || activity.getStatus().equals(Constant.ACTIVITY_END)) {
            return new RestResult<>(EnumError.ACTIVITY_NOT_PROCESSING);
        }

        if (wordFilterUtil.sensitiveLevel(popMessageVo.getMessage()) > 0) {
            return new RestResult<>(EnumError.MESSAGE_SENSITIVE);
        }
        socketIoService.popMessage(userVo, popMessageVo.getMessage());
        return new RestResult<>(EnumError.SUCCESS);
    }
}

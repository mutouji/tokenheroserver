package org.delphy.tokenheroserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.delphy.tokenheroserver.common.Constant;
import org.delphy.tokenheroserver.common.EnumError;
import org.delphy.tokenheroserver.common.RestResult;
import org.delphy.tokenheroserver.entity.Activity;
import org.delphy.tokenheroserver.pojo.*;
import org.delphy.tokenheroserver.service.ActivityService;
import org.delphy.tokenheroserver.service.OracleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author mutouji
 */
@Api(value = "ActivityController", description = "活动接口", tags={"活动"})
@RestController
public class ActivityController {
    private ActivityService activityService;
    private OracleService oracleService;

    public ActivityController(@Autowired ActivityService activityService,
                              @Autowired OracleService oracleService) {
        this.activityService = activityService;
        this.oracleService = oracleService;
    }

    @ApiOperation(value="获取当前活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid", value = "token", paramType = "header", dataType = "string", required = true),
            @ApiImplicitParam(name="mode",value="0 小程序活动,1 H5活动", paramType = "path", dataType="string", required = true, defaultValue = "1")
    })
    @GetMapping("/mainactivity/{mode}")
    public RestResult<Activity> getMainActivity(@PathVariable Long mode) {
        if (mode == null || mode < Constant.ACTIVITY_MODE_WX || mode > Constant.ACTIVITY_MODE_H5) {
            return new RestResult<>(EnumError.ACTIVITY_MODE_ERROR);
        }
        Activity activity = activityService.getMainActivityFromCache(mode);
        if (activity == null || activity.getStatus().equals(Constant.ACTIVITY_END)) {
            activity = activityService.selectMainActivity(mode);
        }
        return new RestResult<>(EnumError.SUCCESS, activity);
    }

    @ApiOperation(value="更新当前活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid", value = "token", paramType = "header", dataType = "string", required = true),
            @ApiImplicitParam(name="mode",value="0 小程序活动,1 H5活动", paramType = "path", dataType="string", required = true, defaultValue = "1")
    })
    @GetMapping("/update/mainactivity/{mode}")
    public RestResult<Activity> updateMainActivity(@PathVariable Long mode) {
        if (mode == null || mode < Constant.ACTIVITY_MODE_WX || mode > Constant.ACTIVITY_MODE_H5) {
            return new RestResult<>(EnumError.ACTIVITY_MODE_ERROR);
        }
        Activity activity = activityService.selectMainActivity(mode);
        return new RestResult<>(EnumError.SUCCESS, activity);
    }


    @ApiOperation(value="获取当天的赛程表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid", value = "token", paramType = "header", dataType = "string", required = true),
            @ApiImplicitParam(name="mode",value="0 小程序活动,1 H5活动", paramType = "path", dataType="string", required = true, defaultValue = "1")
    })
    @GetMapping("/todayactivities/{mode}")
    public RestResult<List<Activity>> getTodayActivities(@PathVariable Long mode) {
        if (mode == null || mode < Constant.ACTIVITY_MODE_WX || mode > Constant.ACTIVITY_MODE_H5) {
            return new RestResult<>(EnumError.ACTIVITY_MODE_ERROR);
        }
        List<Activity> activities = activityService.getTodayActivities(mode);
        return new RestResult<>(EnumError.SUCCESS, activities);
    }

    @ApiOperation(value="获取实时价格等")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid", value = "token", paramType = "header", dataType = "string", required = true),
            @ApiImplicitParam(name="mode",value="0 小程序活动,1 H5活动", paramType = "path", dataType="string", required = true, defaultValue = "1")
    })
    @GetMapping("/activitycurrent/{mode}")
    public RestResult<String> getCurrent(@PathVariable Long mode) {
        Activity activity = activityService.getMainActivityFromCache(mode);

        if (activity == null) {
            return new RestResult<>(EnumError.ACTIVITY_NULL);
        }

        if (activity.getStatus().equals(Constant.ACTIVITY_INIT) || activity.getStatus().equals(Constant.ACTIVITY_END)) {
            return new RestResult<>(EnumError.ACTIVITY_INVALID_STATUS, null);
        }
        OracleNewsVo oracleNewsVo = oracleService.getNews(activity.getOracleId());
        if (oracleNewsVo.getCode() != 0) {
            return new RestResult<>(EnumError.ORACLE_CODE.getCode(), "oracle error code = " + oracleNewsVo.getCode());
        }

        return new RestResult<>(EnumError.SUCCESS, oracleNewsVo.getData().get(0));
    }

    @ApiOperation(value="获取我的结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid", value = "token", paramType = "header", dataType = "string", required = true),
            @ApiImplicitParam(name="activityId",value="活动id", paramType = "path", dataType="string", required = true, defaultValue = "1")
    })
    @GetMapping("/myactivityresult/{activityId}")
    public RestResult<ResultVo> getMyResult(@ApiIgnore UserVo userVo,@PathVariable String activityId) {
        Activity activity = activityService.getActivity(activityId);

        if (activity == null) {
            return new RestResult<>(EnumError.ACTIVITY_NULL);
        }

        if (!activity.getIsSettlement().equals(Constant.LONG_TRUE)) {
            return new RestResult<>(EnumError.ACTIVITY_UNSETTLEMENTED);
        }

        ResultVo resultVo = activityService.getMyResult(activity, userVo);

        return new RestResult<>(EnumError.SUCCESS, resultVo);
    }
}

package org.delphy.tokenheroserver.service;

import lombok.extern.slf4j.Slf4j;
import org.delphy.tokenheroserver.common.Constant;
import org.delphy.tokenheroserver.entity.Activity;
import org.delphy.tokenheroserver.entity.Forecast;
import org.delphy.tokenheroserver.pojo.ResultVo;
import org.delphy.tokenheroserver.pojo.UserVo;
import org.delphy.tokenheroserver.repository.ComplexActivityRepository;
import org.delphy.tokenheroserver.repository.IActivityRepository;
import org.delphy.tokenheroserver.repository.IForecastRepository;
import org.delphy.tokenheroserver.util.RedisUtil;
import org.delphy.tokenheroserver.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mutouji
 */
@Slf4j
@Service
public class ActivityService {
    private RedisUtil redisUtil;
    private ComplexActivityRepository complexActivityRepository;
    private IActivityRepository activityRepository;
    private IForecastRepository forecastRepository;

    public ActivityService(@Autowired RedisUtil redisUtil,
                           @Autowired IActivityRepository activityRepository,
                           @Autowired IForecastRepository forecastRepository,
                           @Autowired ComplexActivityRepository complexActivityRepository) {
        this.complexActivityRepository = complexActivityRepository;
        this.redisUtil = redisUtil;
        this.activityRepository = activityRepository;
        this.forecastRepository = forecastRepository;
    }

    public Activity selectMainActivity(Long mode) {
        Activity activity = this.complexActivityRepository.getMainActivity(mode);
        this.redisUtil.setObject(Constant.CACHE_MAINACTIVITY + mode, activity);
        return activity;
    }

    public Activity getMainActivityFromCache(Long mode) {
        return this.redisUtil.getObject(Constant.CACHE_MAINACTIVITY + mode);
    }

    public void updateMainActivity(Activity activity, Long mode) {
        Long time = TimeUtil.getCurrentSeconds();
        log.info("time:" + time.toString()+", activity changed:" + activity);
        this.activityRepository.save(activity);
        this.redisUtil.setObject(Constant.CACHE_MAINACTIVITY + mode, activity);
    }

    public Activity getActivity(String id) {
        return this.activityRepository.findActivityById(id);
    }

    public List<Activity> getTodayActivities(Long mode) {
        Long begin = TimeUtil.getDayBeginSeconds();
        Long end = begin + Constant.TIME_ADAY_SECONDS;
        return this.complexActivityRepository.getTodayActivities(mode, begin, end);
    }

    public ResultVo getMyResult(Activity activity, UserVo userVo) {
        Forecast forecast = forecastRepository.findByUserIdAndActivityId(userVo.getId(), activity.getId());
        double low = activity.getMinWin();
        double high = activity.getMaxWin();
        Long count = complexActivityRepository.getWinnerCount(activity.getId(), low, high);
        ResultVo resultVo = new ResultVo();
        resultVo.buildResultVo(activity, forecast, count);

        return resultVo;
    }
}

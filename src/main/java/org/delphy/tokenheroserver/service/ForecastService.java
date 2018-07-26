package org.delphy.tokenheroserver.service;

import lombok.extern.slf4j.Slf4j;
import org.delphy.tokenheroserver.common.Constant;
import org.delphy.tokenheroserver.common.EnumError;
import org.delphy.tokenheroserver.common.RestResult;
import org.delphy.tokenheroserver.entity.Activity;
import org.delphy.tokenheroserver.entity.Forecast;
import org.delphy.tokenheroserver.pojo.*;
import org.delphy.tokenheroserver.repository.ComplexActivityRepository;
import org.delphy.tokenheroserver.repository.IActivityRepository;
import org.delphy.tokenheroserver.repository.IForecastRepository;
import org.delphy.tokenheroserver.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mutouji
 */
@Slf4j
@Service
public class ForecastService {
    private IForecastRepository forecastRepository;
    private ActivityService activityService;
    private IActivityRepository activityRepository;
    private ComplexActivityRepository complexActivityRepository;

    public ForecastService(@Autowired IForecastRepository forecastRepository,
                           @Autowired IActivityRepository activityRepository,
                           @Autowired ComplexActivityRepository complexActivityRepository,
                           @Autowired ActivityService activityService) {
        this.forecastRepository = forecastRepository;
        this.activityService = activityService;
        this.activityRepository = activityRepository;
        this.complexActivityRepository = complexActivityRepository;
    }

    public List<ForecastResultsVo> getForecasts(String userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Forecast> forecasts = this.forecastRepository.findByUserIdAndRewardTimeGreaterThanOrderByIdDesc(userId, 0L, pageable);
        List<ForecastResultsVo> forecastResultsVos = new ArrayList<>();
        if (forecasts == null || forecasts.size() == 0) {
            return forecastResultsVos;
        }
        List<String> activityIds = new ArrayList<>(forecasts.size());
        for (Forecast forecast : forecasts) {
            activityIds.add(forecast.getActivityId());
        }
        List<Activity> activities = this.activityRepository.findByIdIn(activityIds);
        Map<String, Activity> activityMap = new HashMap<>(activities.size());
        for (Activity activity : activities) {
            activityMap.put(activity.getId(), activity);
        }

        for (Forecast forecast : forecasts) {
            Activity activity = activityMap.get(forecast.getActivityId());
            ForecastResultsVo forecastResultsVo = new ForecastResultsVo(forecast, activity);
            forecastResultsVos.add(forecastResultsVo);
        }

        return forecastResultsVos;
    }

    public RestResult<Forecast> trySetForecasts(String userId, ForecastVo forecastVo, Long mode) {
        // 1. 获取当前活动阶段，判断，活动判断是否是有效预测
        // 2. 获取用户在当前市场内的可用的定价次数，如果为0，则查看改价卡次数
        // 3. 如果>0, 使用预测一次，并报告定价，并通知所有人
        long time = TimeUtil.getCurrentSeconds();
        Activity activity =  activityService.getMainActivityFromCache(mode);
        if (activity == null || !activity.getStatus().equals(Constant.ACTIVITY_PROCESSING)) {
            return new RestResult<>(EnumError.ACTIVITY_INVALID_STATUS);
        }
        long begin = activity.getStart();
        long hold = begin + activity.getHold() * 60;
        if (time < begin || time >= hold) {
            return new RestResult<>(EnumError.ACTIVITY_INVALID_TIME);
        }
        // 需要事务操作
        Forecast forecast = forecastRepository.findByUserIdAndActivityId(userId, activity.getId());
        boolean isNew = false;
        if (forecast == null) {
            forecast = new Forecast(userId, activity.getId(), time);
            isNew = true;
        }
        if (forecast.getFreeNum() <= 0) {
            return new RestResult<>(EnumError.ACTIVITY_OUTOF_PRECAST_TIMES);
        }
        forecastVo.initForecast(activity, forecast, TimeUtil.getCurrentSeconds());
        if (isNew) {
            forecastRepository.insert(forecast);
        } else {
            forecastRepository.save(forecast);
        }
        return new RestResult<>(EnumError.SUCCESS, forecast);
    }

    public Forecast getLastForecast(String userId, Long mode) {
        Activity activity =  activityService.getMainActivityFromCache(mode);
        if (activity == null) {
            return null;
        }
        return forecastRepository.findByUserIdAndActivityId(userId, activity.getId());
    }

    Forecast getForecast(Activity activity, String userId) {
        if (activity == null) {
            return null;
        }
        return forecastRepository.findByUserIdAndActivityId(userId, activity.getId());
    }

    @Caching(evict = {
            @CacheEvict(value = "forecast", allEntries = true),
            @CacheEvict(value = "user", allEntries = true),
            @CacheEvict(value = "userPosition", allEntries = true),
            @CacheEvict(value = "userTop10", key="'top10'")
    })
    public boolean doSettlement(Activity activity) {
        // 先从数据库获取值，如果有，就用数据库中的值做判断
        Activity activityDB = this.activityRepository.findActivityById(activity.getId());
        double result = activityDB.getResult();

        if (result > 0) {
            double low = activity.getMinWin();
            double high = activity.getMaxWin();
            List<Forecast> forecasts = this.forecastRepository.findByActivityId(activity.getId());
            List<Forecast> winners = new ArrayList<>();
            List<Forecast> lossers = new ArrayList<>();

            double total = activity.getPond();
            Long rewardTime = activity.getGetOracleTime();
            for (Forecast forecast : forecasts) {
                forecast.setRewardTime(rewardTime);
                forecast.setReward(0.0);
                if (forecast.isWin(low, high)) {
                    winners.add(forecast);
                } else {
                    lossers.add(forecast);
                }
            }

            for (Forecast forecast : winners) {
                double reward = Double.parseDouble(String.format("%.2f", forecast.getRewardRatio() * (total / winners.size() ) - 0.005));
                if (reward < 0.01) { reward = 0.01;}
                forecast.setReward(reward);
            }

            updateRewards(winners, lossers);
            activity.setResult(result);
            activity.setIsSettlement(Constant.LONG_TRUE);
            activity.setStatus(Constant.ACTIVITY_END);
            return true;
        }
        return false;
    }

    private void updateRewards(List<Forecast> winners, List<Forecast> lossers) {
        if (winners != null && winners.size() > 0) {
            complexActivityRepository.updateForecasts(winners);
            complexActivityRepository.updateUsers(winners, true);
        }
        if (lossers != null && lossers.size() > 0) {
            complexActivityRepository.updateForecasts(lossers);
            complexActivityRepository.updateUsers(lossers, false);
        }
    }
}

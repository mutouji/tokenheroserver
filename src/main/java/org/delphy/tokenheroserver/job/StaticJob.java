package org.delphy.tokenheroserver.job;

import lombok.extern.slf4j.Slf4j;
import org.delphy.tokenheroserver.common.Constant;
import org.delphy.tokenheroserver.entity.Activity;
import org.delphy.tokenheroserver.service.ActivityService;
import org.delphy.tokenheroserver.service.ForecastService;
import org.delphy.tokenheroserver.service.SocketIoService;
import org.delphy.tokenheroserver.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author mutouji
 */
@Slf4j
@Component
public class StaticJob {
    private final static long SECOND = 1000;
    private ActivityService activityService;
    private ForecastService forecastService;
    private SocketIoService socketIoService;
    private static Activity lastActivity;

    public StaticJob(@Autowired ActivityService activityService,
                     @Autowired SocketIoService socketIoService,
                     @Autowired ForecastService forecastService) {
        this.activityService = activityService;
        this.forecastService = forecastService;
        this.socketIoService = socketIoService;
    }

    @Scheduled(initialDelay = 4 * SECOND, fixedRate = 3 * SECOND)
    void updateActivityStatus() {
        Activity activity = activityService.selectMainActivity(Constant.ACTIVITY_MODE_H5);
        if (activity == null) {
            return;
        }
        if (lastActivity == null || !lastActivity.getId().equals(activity.getId())) {
            lastActivity = activity;
            socketIoService.sendAllNewActivity();
        }
        long time = TimeUtil.getCurrentSeconds();
        Long oldStatus = activity.getStatus();
        Long status = activity.getCurrentStatus(time);
        if (status > oldStatus) {
            activity.setStatus(status);
            this.activityService.updateMainActivity(activity, Constant.ACTIVITY_MODE_H5);
        }

        if (oldStatus.equals(Constant.ACTIVITY_CLEARING)) {
            if (time + 3 >= activity.getEnd()) {
                if (activity.getIsSettlement().equals(Constant.LONG_FALSE)) {
                    // in doSettlement will change activity settlement state if success
                    if (this.forecastService.doSettlement(activity)) {
                        this.activityService.updateMainActivity(activity, Constant.ACTIVITY_MODE_H5);
                        activityService.selectMainActivity(Constant.ACTIVITY_MODE_H5);
                    }
                }
            }
        }

        if (Constant.ACTIVITY_PROCESSING.equals(status) || Constant.ACTIVITY_LOCKED.equals(status)) {
            this.socketIoService.sendAllNews(activity);
        }
    }
}

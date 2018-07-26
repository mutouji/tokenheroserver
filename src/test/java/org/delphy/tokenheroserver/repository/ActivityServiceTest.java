package org.delphy.tokenheroserver.repository;

import org.delphy.tokenheroserver.common.Constant;
import org.delphy.tokenheroserver.entity.Activity;
import org.delphy.tokenheroserver.entity.Forecast;
import org.delphy.tokenheroserver.entity.Price;
import org.delphy.tokenheroserver.entity.User;
import org.delphy.tokenheroserver.pojo.ForecastVo;
import org.delphy.tokenheroserver.service.ActivityService;
import org.delphy.tokenheroserver.service.ForecastService;
import org.delphy.tokenheroserver.service.IndexService;
import org.delphy.tokenheroserver.util.TimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityServiceTest {
    @Autowired
    private IActivityRepository activityRepository;
    @Autowired
    private ActivityService activityService;

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IndexService indexService;

    @Autowired
    private IForecastRepository forecastRepository;

    @Autowired
    private ComplexActivityRepository complexActivityRepository;

    @Test
    public void getMainActivity() {
//        Activity activity = activityRepository.getMainActivity(1538800000L);
        Activity activity = complexActivityRepository.getMainActivity(Constant.ACTIVITY_MODE_H5);
        System.out.println(activity);
    }

    @Test
    public void testForecast() {
        long start = 1530065100L;
        double result = 6064.98;

        Activity activity = activityRepository.findActivityById("15319083725969899");
        for (int i = 0; i < 1; i++) {
            String userId = "15319726267333044";
            Forecast forecast = new Forecast(userId, activity.getId(), start + i);
            ForecastVo forecastVo = new ForecastVo();
            forecastVo.setPrice(result - 1.0);
            forecastVo.initForecast(activity, forecast, activity.getStart() + 60);
            forecastRepository.save(forecast);
        }
    }

    @Test
    public void testFindByPhone() {
        // @Cacheable(value = "user", key="#p0")
        User user = userRepository.findByPhone("13436665547");
        System.out.println(user.toString());
    }

    @Test
    public void testTop10() {
        // @Cacheable(value = "top10", key="'top10'")
        List<User> users10 = userRepository.findTop10ByTotalDpyGreaterThanOrderByTotalDpyDesc(0.0);
        for(User u : users10) {
            System.out.println(u.toString());
        }
    }

    @Test
    public void testUserForecasts() {
        Integer page = 0;
        Integer size = 2;
        Pageable pageable = PageRequest.of(page, size);
        List<Forecast> forecasts = forecastRepository.findByUserIdAndRewardTimeGreaterThanOrderByIdDesc("15294723414325314",0L, pageable);

        for(Forecast f : forecasts) {
            System.out.println(f.toString());
        }
    }

    @Test
    public void testUserForecastInOneActivity() {
        Forecast forecast = forecastRepository.findByUserIdAndActivityId("15294723414325314", "15302558676657375");
        System.out.println(forecast.toString());
    }

    @Test
    public void testActivity() {
        Activity activity = activityRepository.findActivityById("15308616793126118");
        System.out.println(activity.toString());
    }

    @Test
    public void testTodayActivities() {
        Long begin = TimeUtil.getDateSeconds("2018-07-06");
        Long end = begin + Constant.TIME_ADAY_SECONDS;
        List<Activity> activities = complexActivityRepository.getTodayActivities(Constant.ACTIVITY_MODE_H5, begin, end);
        for ( Activity activity : activities) {
            System.out.println(activity.toString());
        }
    }

    @Test
    public void testUpdateActivity() {
        Activity activity = activityRepository.findActivityById("15308616793126118");
        activity.setStatus(Constant.ACTIVITY_PROCESSING);
        activityService.updateMainActivity(activity, activity.getMode());
    }

    @Test
    public void testTrySetForecasts() {
        Forecast forecast = forecastRepository.findByUserIdAndActivityId("15294723414325314", "15302558676657375");
        forecast.setLast(new Price(555.0, 0.5, 1529472341L));
        forecastRepository.save(forecast);
    }

    @Test
    public void testGenerateUser() {
        User user = indexService.generateUser("12345555666");
        System.out.println(user.toString());
    }
}

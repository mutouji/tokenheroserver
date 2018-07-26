package org.delphy.tokenheroserver.service;

import org.delphy.tokenheroserver.entity.Activity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityServiceTest {
    @Autowired
    private ActivityService activityService;

    @Test
    public void testGetMainActivity() {
//        Activity activity = activityService.selectMainActivity(1L);
//        System.out.println(activity);
        Activity activity = activityService.getMainActivityFromCache(1L);
        System.out.println(activity);
    }
}

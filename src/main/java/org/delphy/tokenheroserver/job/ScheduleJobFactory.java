package org.delphy.tokenheroserver.job;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * @author mutouji
 */
@Component
public class ScheduleJobFactory extends AdaptableJobFactory {
//    private AutowireCapableBeanFactory autowireCapableBeanFactory;
//
//    public ScheduleJobFactory(@Autowired AutowireCapableBeanFactory autowireCapableBeanFactory) {
//        this.autowireCapableBeanFactory = autowireCapableBeanFactory;
//    }
//
//    /**
//     * 将 JobFactory 注入 Spring 容器中，用于依赖管理，否则会导致 Job 中注入的 Bean 为 null
//     * @param bundle bundle
//     * @return bundle
//     * @throws Exception bundle
//     */
//    @Override
//    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
//        Object jobInstance = super.createJobInstance(bundle);
//        autowireCapableBeanFactory.autowireBean(jobInstance);
//        return jobInstance;
//    }
}

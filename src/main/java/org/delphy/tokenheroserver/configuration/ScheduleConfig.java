package org.delphy.tokenheroserver.configuration;

import org.delphy.tokenheroserver.job.ScheduleJobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author mutouji
 */
@Configuration
public class ScheduleConfig {
    private ScheduleJobFactory jobFactory;

    public ScheduleConfig(@Autowired ScheduleJobFactory jobFactory) {
        this.jobFactory = jobFactory;
    }

    /**
     * To Configuration Quartz , not necessary, if not config this, will use default
     * param dataSource dataSource:  @Qualifier("dataSource") DataSource dataSource
     * @return return ：schedulerFactoryBean.setDataSource(dataSource);
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setSchedulerName("task_executor");
        schedulerFactoryBean.setStartupDelay(10);
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContextKey");
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setJobFactory(jobFactory);
        return schedulerFactoryBean;
    }
}

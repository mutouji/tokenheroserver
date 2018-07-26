package org.delphy.tokenheroserver.job;

import lombok.extern.slf4j.Slf4j;
import org.delphy.tokenheroserver.exception.ServiceException;
import org.quartz.*;

/**
 * Schedule utils for manage Quartz Job
 * @author mutouji
 */
@Slf4j
public class ScheduleUtil {
//    /**
//     * Get Trigger key
//     * @param scheduleJob scheduleJob
//     * @return scheduleJob
//     */
//    public static TriggerKey getTriggerKey(ScheduleJob scheduleJob) {
//        return TriggerKey.triggerKey(scheduleJob.getTriggerName(), scheduleJob.getTriggerGroup());
//    }
//
//    /**
//     * Get Job Key
//     * @param scheduleJob scheduleJob
//     * @return return
//     */
//    public static JobKey getJobKey(ScheduleJob scheduleJob) {
//        return JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
//    }
//
//    /**
//     * Get Cron Expression trigger
//     * @param scheduler scheduler
//     * @param scheduleJob scheduleJob
//     * @return return
//     * @throws ServiceException ServiceException
//     */
//    public static CronTrigger getCronTrigger(Scheduler scheduler, ScheduleJob scheduleJob) throws ServiceException {
//        try {
//            return (CronTrigger) scheduler.getTrigger(getTriggerKey(scheduleJob));
//        } catch (SchedulerException e) {
//            throw new ServiceException("Get Cron trigger failed", e);
//        }
//    }
//
//    public static void createScheduleJob(Scheduler scheduler, ScheduleJob  scheduleJob) throws ServiceException {
//        validateCronExpression(scheduleJob);
//
//        try {
//            Class<? extends Job> jobClass = (Class<? extends Job>)Class.forName(scheduleJob.getClassName()).newInstance().getClass();
//            // Get Job class from ScheduleJob entity
//            JobDetail jobDetail = JobBuilder.newJob(jobClass)
//                    .withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
//                    .withDescription(scheduleJob.getDescription())
//                    .build();
//
//            // Create CronScheduleBuilder entity
//            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
//                    .withMisfireHandlingInstructionDoNothing();
//
//            // Create a new CronTrigger by Cron Expression
//            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
//                    .withIdentity(scheduleJob.getTriggerName(), scheduleJob.getTriggerGroup())
//                    .withDescription(scheduleJob.getDescription())
//                    .withSchedule(scheduleBuilder)
//                    .startNow()
//                    .build();
//
//            scheduler.scheduleJob(jobDetail, cronTrigger);
//
//            if (scheduleJob.isPause()) {
//                pauseJob(scheduler, scheduleJob);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("Execute schedule job failed");
//            throw new ServiceException("Execute schedule job failed", e);
//        }
//    }
//
//    public static void updateScheduleJob(Scheduler scheduler, ScheduleJob scheduleJob) throws ServiceException {
//        try {
//            TriggerKey triggerKey = getTriggerKey(scheduleJob);
//            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
//                    .withMisfireHandlingInstructionDoNothing();
//
//            CronTrigger cronTrigger = getCronTrigger(scheduler, scheduleJob);
//
//            // Create new cron trigger
//            cronTrigger = cronTrigger.getTriggerBuilder()
//                    .withIdentity(triggerKey)
//                    .withDescription(scheduleJob.getDescription())
//                    .withSchedule(cronScheduleBuilder)
//                    .build();
//
//            scheduler.rescheduleJob(triggerKey, cronTrigger);
//
//            log.info("Update schedule job {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());
//
//            if (scheduleJob.isPause()) {
//                pauseJob(scheduler, scheduleJob);
//            }
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//            log.error("Update schedule job failed");
//            throw new ServiceException("Update schedule job failed", e);
//        }
//    }
//
//    public static void run(Scheduler scheduler, ScheduleJob scheduleJob) throws ServiceException {
//        try {
//            scheduler.triggerJob(getJobKey(scheduleJob));
//            log.info("Run schedule job {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//            log.error("Run schedule job failed");
//            throw new ServiceException("Run schedule job failed", e);
//        }
//    }
//
//    public static void pauseJob(Scheduler scheduler, ScheduleJob scheduleJob) throws ServiceException {
//        try {
//            scheduler.pauseJob(getJobKey(scheduleJob));
//            log.info("Pause schedule job {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//            log.error("Pause schedule job failed");
//            throw new ServiceException("Pause job failed", e);
//        }
//    }
//
//    public static void resumeJob(Scheduler scheduler, ScheduleJob scheduleJob) throws ServiceException {
//        try {
//            scheduler.resumeJob(getJobKey(scheduleJob));
//            log.info("Resume schedule job {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//            log.error("Resume schedule job failed");
//            throw new ServiceException("Resume job failed", e);
//        }
//    }
//
//    public static void deleteJob(Scheduler scheduler, ScheduleJob scheduleJob) throws ServiceException {
//        try {
//            scheduler.deleteJob(getJobKey(scheduleJob));
//            log.info("Delete schedule job {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//            log.error("Delete schedule job failed");
//            throw new ServiceException("Delete job failed", e);
//        }
//    }
//
//    public static void validateCronExpression(ScheduleJob scheduleJob) throws ServiceException {
//        if (!CronExpression.isValidExpression(scheduleJob.getCronExpression())) {
//            throw new ServiceException(String.format("ob %s expression %s is not correct!", scheduleJob.getClassName(), scheduleJob.getCronExpression()));
//        }
//    }
}

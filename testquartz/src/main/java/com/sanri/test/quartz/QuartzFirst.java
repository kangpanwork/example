package com.sanri.test.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzFirst {
    public static void main(String[] args) throws SchedulerException {
        // 获取调度器
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();

        // 创建 JobDetail
        JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class)
                .withIdentity("firstJob","$9420").build();

        // 创建触发器
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("firstTrigger","$9420")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(20).repeatForever())
                .build();

        //加入任务调度
        scheduler.scheduleJob(jobDetail,trigger);

//        scheduler.shutdown();
    }
}

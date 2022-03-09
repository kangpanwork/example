package com.sanri.test.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzFirstOrigin {
    public static void main(String[] args) throws SchedulerException {
        //获取调度器
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        //启动调度器
        scheduler.start();
        scheduler.shutdown();
    }
}


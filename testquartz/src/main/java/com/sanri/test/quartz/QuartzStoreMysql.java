package com.sanri.test.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzStoreMysql {
    public static void main(String[] args) throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory("quartzdb.properties");
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();
    }
}

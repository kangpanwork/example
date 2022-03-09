package com.sanri.test.quartz;

import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzFromConfig {
    public static void main(String[] args) throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory("quartz.properties");
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();
    }
}

package com.sanri.test.quartz;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.*;

/**
 * 保证上一个任务没执行完不并行执行，串行 ，
 * 比如定的时间为 2 分钟执行一次，但执行一次需要 5 分钟
 *
 */
@DisallowConcurrentExecution
public class SimpleJob implements Job {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(this+"触发时间:"+ DateFormatUtils.ISO_DATETIME_FORMAT.format(System.currentTimeMillis()));
    }
}

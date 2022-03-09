package com.sanri.test.netty;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * netty 的延时队列
 */
public class DelayQueueMain {
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Test
    public void testDelayQueue() throws InterruptedException {
        ThreadFactory factory = r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("RedPacketHashedWheelTimerWorker");
            return thread;
        };
        /**
         * @param tickDuration - 每tick一次的时间间隔
         * @param unit - tickDuration 的时间单位
         * @param ticksPerWheel - 时间轮中的槽数
         * @param leakDetection - 检查内存溢出
         *
         * 100 个槽 , 每个槽 1 秒 ,代表 100 s
         */
        Timer timer = new HashedWheelTimer(factory, 1,TimeUnit.SECONDS, 100,true);
        System.out.println(String.format("开始任务时间:%s", LocalDateTime.now().format(F)));

        // 每后设定 1 到 9 秒的一个延时任务
        for(int i=1;i<10;i++){
            TimerTask timerTask = new RedPacketTimerTask(i);
            timer.newTimeout(timerTask, i, TimeUnit.SECONDS);
        }
        Thread.sleep(Integer.MAX_VALUE);
    }
}

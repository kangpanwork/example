package com.sanri.test.juc;

import org.apache.commons.collections.list.SynchronizedList;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class AQSMain {
    @Test
    public void testQueue() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        CountDownLatch countDownLatch = new CountDownLatch(10);


        for (int i = 0; i < 10; i++) {
            RunThread runThread = new RunThread(countDownLatch);
            new Thread(runThread).start();
        }
        countDownLatch.await();
        System.out.println();
    }

    static class RunThread implements Runnable{
        private CountDownLatch countDownLatch;

        public RunThread(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
            System.out.println("我也不知道我在干什么");
        }
    }

}

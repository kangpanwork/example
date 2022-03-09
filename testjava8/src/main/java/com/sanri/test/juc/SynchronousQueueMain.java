package com.sanri.test.juc;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import java.util.concurrent.*;

public class SynchronousQueueMain {
    static SynchronousQueue blockQueue = new SynchronousQueue();
//    static ArrayBlockingQueue blockQueue = new ArrayBlockingQueue(15);

    static class Producer extends Thread{
        @Override
        public void run() {
            try {
                producerConsumerCyclicBarrier.await();

                for (int i = 0; i < 10; i++) {
                    int randomSleep = RandomUtils.nextInt(500, 2000);
                    Thread.sleep(randomSleep);
                    blockQueue.put("sanri+"+randomSleep);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    // 用于 10 个消息线程同时开始启动
    static CyclicBarrier cyclicBarrier = new CyclicBarrier(10);

    // 用于生产者消费者同时开始工作
    static CyclicBarrier producerConsumerCyclicBarrier = new CyclicBarrier(2);

    // 这个用于等 10 个线程同时消费完成后关闭主进程和 stopwatch
    static CountDownLatch countDownLatch = new CountDownLatch(10);

    static class Consumer extends Thread{
        StopWatch stopWatch ;
        CountDownLatch countDownLatch ;
        public Consumer(StopWatch stopWatch, CountDownLatch countDownLatch) {
            this.stopWatch = stopWatch;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            try {
                Object peek = blockQueue.take();
                System.out.println(Thread.currentThread().getName()+" 获取到数据:"+peek+",当前时间(相对于启动) :"+stopWatch.getTime()+" ms");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }
    }

    static class ConsumerAll extends Thread{
        StopWatch stopWatch;
        public ConsumerAll(StopWatch stopWatch) {
            this.stopWatch = stopWatch;
        }

        @Override
        public void run() {
            try {
                producerConsumerCyclicBarrier.await();

                for (int i = 0; i < 10; i++) {
                    new Consumer(stopWatch,countDownLatch).start();
                }
                System.out.println(" 10 个线程启动用时:"+stopWatch.getTime()+" ms");

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testBlockQueue() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();stopWatch.start();
        // 启一个生产者生产 10 条消息,这个生产者是比较慢的
        new Producer().start();
        new ConsumerAll(stopWatch).start();

        countDownLatch.await();

        stopWatch.stop();
        System.out.println("打印完总共用时:"+stopWatch.getTime()+" ms");
    }
}

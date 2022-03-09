package com.sanri.test.testrediscluster;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LockTests {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void testLock() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        LockTest lockTest = new LockTest(countDownLatch);
        new Thread(lockTest).start();
        countDownLatch.await();
    }

    // 线程池里面用并发控制非常危险, 如果线程进入排队,加入 CyclicBarrier ,则会造成互相等待的情况
//    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 5, 0, TimeUnit.MILLISECONDS,
//                    new ArrayBlockingQueue<>(10), new ThreadFactory() {
//        @Override
//        public Thread newThread(Runnable r) {
//            Thread thread = new Thread(r);
//            thread.setDaemon(true);
//            thread.setName("LockTest");
//            return thread;
//        }
//    });

    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 15, 0, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(10), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("LockTest");
            return thread;
        }
    });

    class LockTest implements Runnable{
        private CountDownLatch countDownLatch;

        public LockTest(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            CountDownLatch mainThreadTerminal = new CountDownLatch(10);
            try{
                CyclicBarrier cyclicBarrier = new CyclicBarrier(10);
                // 发起 10 个并发去获取同一把锁
                for (int i = 0; i < 10; i++) {
                    int finalI = i;
                    threadPoolExecutor.submit(()-> {
                        RLock lock = null;
                        try {
                            cyclicBarrier.await();
                            lock = redissonClient.getLock("lock");
                            lock.lock(100000,TimeUnit.HOURS);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            System.out.println("线程 "+Thread.currentThread().getName()+"获取到锁 lock "+simpleDateFormat.format(new Date()));
                            // 业务操作 , 模拟等待 2s
                            TimeUnit.SECONDS.sleep(1);
                            if (finalI == 3){
                                TimeUnit.SECONDS.sleep(10);
                            }
                            if (finalI == 7){
                                System.out.println("在 7 时异常退出");
                                System.exit(-1);
                            }
                        } catch (InterruptedException | BrokenBarrierException e) {
                            e.printStackTrace();
                        }finally {
                            System.out.println(finalI+" 释放锁 ");
                            lock.unlock();
                            mainThreadTerminal.countDown();
                        }

                    });
                }
            }finally {
                try {
                    mainThreadTerminal.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("结束了");
                countDownLatch.countDown();
            }
        }
    }



}

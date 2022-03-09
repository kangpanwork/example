package com.sanri.test.locktest;

import org.junit.Test;

import java.util.concurrent.Semaphore;

/**
 * 本类选自文章 https://www.pdai.tech/md/java/thread/java-thread-x-juc-tool-semaphore.html
 */
public class SemaphoreTest {

    /**
     * Semaphore初始化有10个令牌，11个线程同时各调用1次acquire方法，会发生什么？
     * 会有一个线程被阻塞
     */
    @Test
    public void testFirst() throws InterruptedException {
        Semaphore semaphore = new Semaphore(10);
        for (int i = 0; i < 11; i++) {
            int finalI = i;
            new Thread(){
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        System.out.println("线程:"+Thread.currentThread().getName()+" 调用获取令牌成功:"+ finalI);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        // 为了让 junit 测试准确
        Thread.currentThread().join();
    }

    /**
     * Semaphore初始化有10个令牌，一个线程重复调用11次acquire方法，会发生什么？
     * 会获取 10 次令牌，最后一个没有令牌了，阻塞
     */
    @Test
    public void testOneThreadRepeat() throws InterruptedException {
        Semaphore semaphore = new Semaphore(10);
        for (int i = 0; i < 11; i++) {
            semaphore.acquire();
            System.out.println("获取令牌成功:"+i);
        }
    }

    @Test
    public void testJoin() throws InterruptedException {
        Thread subThread = new Thread() {
            @Override
            public void run() {
                System.out.println("子线程做的事情");
            }
        };
        subThread.start();

        subThread.join();

        System.out.println("主线程后做事情");
    }

    /**
     * Semaphore初始化有1个令牌，1个线程调用一次acquire方法，然后调用两次release方法，之后另外一个线程调用acquire(2)方法，此线程能够获取到足够的令牌并继续运行吗？
     * 可以，acquire操作视为是消费一个许可，而release操作是创建一个许可，Semaphore并不受限于它在创建时的初始许可数量。
     * @throws InterruptedException
     */
    @Test
    public void testReleaseOverflow() throws InterruptedException {
        Semaphore semaphore = new Semaphore(1);

        semaphore.acquire();
        semaphore.release();
        System.out.println("第一次 release 成功");
        semaphore.release();
        System.out.println("第二次 release 成功");

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    semaphore.acquire(2);
                    System.out.println("调用 acquire(2) 成功");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();

        // 为了让 junit 测试准确
        thread.join();
    }

    /**
     * Semaphore初始化有2个令牌，一个线程调用1次release方法，然后一次性获取3个令牌，会获取到吗？
     * 可以取到， release 就相相于加一个令牌
     */
    @Test
    public void testReleaseAdd() throws InterruptedException {
        Semaphore semaphore = new Semaphore(2);
        semaphore.release();
        semaphore.acquire(3);
    }
}

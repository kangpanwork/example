package com.sanri.test.locktest;

import org.junit.Test;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTes {

    /**
     * 会去检测是否是同一个线程来做到可重入
     */
    @Test
    public void testQueue(){
        ReentrantLock reentrantLock = new ReentrantLock(true);
        reentrantLock.lock();
        reentrantLock.lock();
        reentrantLock.lock();
    }

    /**
     * synchronized 是可重入的
     * @throws InterruptedException
     */
    @Test
    public void testSynchronized() throws InterruptedException {
        int deep = 0 ;
        methodParent(deep);
    }

    public void methodParent(int deep) throws InterruptedException {
        if (deep > 10)return;
        synchronized (ReentrantLockTes.class) {
            System.out.println("methodParent"+Thread.currentThread().getName());
            methodChild(++deep);
        }
    }

    public void methodChild(int deep) throws InterruptedException {
        if (deep > 10)return;

        synchronized (ReentrantLockTes.class) {
            System.out.println("methodChild"+Thread.currentThread().getName());
            methodParent(++deep);
        }
    }
}

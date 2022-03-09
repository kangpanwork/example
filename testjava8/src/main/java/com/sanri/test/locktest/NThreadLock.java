package com.sanri.test.locktest;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NThreadLock {

    static class PrintThread extends Thread{
        private int i;

        public PrintThread(int i){this.i = i;}

        @Override
        public void run() {
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int j = 0; j < 10; j++) {
                lock.lock();
                try{
                    int shouldLast = (i - 1 ) < 0 ? N -1: i-1;
                    if(lastPrint !=  shouldLast ){
                        try {
                            conditions[i].await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.print(i + 1);

                    lastPrint = i;
                    int nextSignal = (i+1) % N;
                    conditions[nextSignal].signal();
                }finally {
                    lock.unlock();
                }
            }
        }
    }

    static final int N = 20;
    private static Lock lock = new ReentrantLock();
    private static Condition [] conditions = new Condition[N];
    static CyclicBarrier cyclicBarrier = new CyclicBarrier(N);

    static int lastPrint = N - 1;

    public static void main(String[] args) {
        for (int i = 0; i < N; i++) {
            conditions[i] = lock.newCondition();
        }
        for (int i = 0; i < N; i++) {
            new PrintThread(i).start();
        }
    }
}

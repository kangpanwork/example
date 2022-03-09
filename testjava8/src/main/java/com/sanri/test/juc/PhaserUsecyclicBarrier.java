package com.sanri.test.juc;

import java.util.concurrent.CyclicBarrier;

public class PhaserUsecyclicBarrier {
    public static void main(String[] args) throws InterruptedException {
        int parties = 3;
        int phases = 4;

        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {

            }
        });

        for (int i = 0; i < parties; i++) {
            int threadId = i;
            int finalI = i;
            Thread thread = new Thread(() -> {
                for (int phase = 0; phase < phases; phase++) {
                    System.out.println(String.format("Thread %s, phase %s", threadId, phase));
                    try {
                        cyclicBarrier.await();
                    }catch (Exception e){}
                    System.out.println("======================");
                    cyclicBarrier.reset();
                }
            });
            thread.start();
        }
    }
}

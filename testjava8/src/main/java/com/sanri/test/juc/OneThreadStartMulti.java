package com.sanri.test.juc;

public class OneThreadStartMulti {
    public static void main(String[] args) {
        Runnable runnable = () -> {
            System.out.println(Thread.currentThread().getName()+"|"+System.currentTimeMillis());
        };

        Thread thread = new Thread(runnable);
        thread.start();
        thread.start();
    }
}

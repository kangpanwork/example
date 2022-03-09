package com.sanri.test.sometest;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureMain {

    /**
     * 任务编排
     * A 任务 和 B 任务同时完成 , 才可以执行 C
     */
    @Test
    public void testTaskArrange(){
        CompletableFuture<Void> taskA = CompletableFuture.runAsync(() -> {
            System.out.println("taskA");
        });
        CompletableFuture<Void> taskB = CompletableFuture.runAsync(() -> {
            System.out.println("taskB");
        });
        CompletableFuture<Void> task = CompletableFuture.allOf(taskA, taskB).thenRun(() -> {
            System.out.println("taskC");
        });
    }

    /**
     * 任务编排
     * A 任务 和 B 任务只要有一个完成  , 就会去执行任务 C
     */
    @Test
    public void testTaskArrange2(){
        CompletableFuture<Void> taskA = CompletableFuture.runAsync(() -> {
            System.out.println("taskA");
        });
        CompletableFuture<Void> taskB = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(100);
                System.out.println("taskB");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        CompletableFuture<Void> task = CompletableFuture.anyOf(taskA, taskB).thenRun(() -> {
            System.out.println("taskC");
        });
    }
}

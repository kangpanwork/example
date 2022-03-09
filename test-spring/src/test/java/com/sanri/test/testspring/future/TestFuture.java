package com.sanri.test.testspring.future;

import org.junit.Test;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.ListenableFutureTask;

import java.util.concurrent.*;

public class TestFuture {
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,5,0, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(10));
    @Test
    public void test() throws BrokenBarrierException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ListenableFutureTask<Integer> listenableFutureTask = new ListenableFutureTask(() -> {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(100);
                System.out.println("当前执行第 "+i+" 条记录");
            }
            return 10 ;
        });
        listenableFutureTask.addCallback(new ListenableFutureCallback<Integer>() {
            @Override
            public void onFailure(Throwable ex) {

            }

            @Override
            public void onSuccess(Integer result) {
                System.out.println("成功了"+result+" 条");
            }
        });

        listenableFutureTask.addCallback(success -> {
            System.out.println(success+" 条记录");

            countDownLatch.countDown();
        },fail -> {
            fail.printStackTrace();

            countDownLatch.countDown();
        });

        threadPoolExecutor.submit(listenableFutureTask);
        countDownLatch.await();
    }
}

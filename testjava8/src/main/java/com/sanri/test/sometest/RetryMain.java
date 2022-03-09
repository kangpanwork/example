package com.sanri.test.sometest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.springframework.retry.*;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.CircuitBreakerRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.DefaultRetryState;
import org.springframework.retry.support.RetryTemplate;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * spring 的重试框架
 *
 * SpringRetry版本只能对异常进行重试，对于自定义对数据结构不能支持，如果有这方面需求的化，可以考虑用GuavaRetry进行重试
 *
 * https://blog.csdn.net/weixin_33785108/article/details/91387183
 * 可以使用方法上注解功能
 */
@Slf4j
public class RetryMain {

    @Test
    public void simpleRetry(){
        RetryTemplate retryTemplate = new RetryTemplate();

        // 设置重试次数
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(3));

        // 设置退避策略
        ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
        exponentialBackOffPolicy.setInitialInterval(1000);
        exponentialBackOffPolicy.setMultiplier(0.5);
        exponentialBackOffPolicy.setMaxInterval(15000);
        retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);

        StopWatch watch = new StopWatch();watch.start();
        // 执行外部请求
        AtomicInteger counter = new AtomicInteger(0);
        Double execute = retryTemplate.execute(retryContext -> {
            // 重试次数为 3 次 , 表示这里的逻辑第三次必须成功, 否则认定为失败
            Throwable lastThrowable = retryContext.getLastThrowable();
            if (lastThrowable != null) {
                log.info("执行失败,上次失败原因为: {},当前耗时:{} ms", lastThrowable.getMessage(), watch.getTime());
            }
            int andIncrement = counter.getAndIncrement();
            if (andIncrement < 2) {
                throw new IllegalArgumentException(counter + "");
            }
            return 100 * Math.random();
        });
        log.info("执行成功,结果为 {} , 耗时: {} ms",execute,watch.getTime());

    }

    /**
     * 可以加入兜底函数, 当最后一次失败时, 返回默认值
     * @throws Exception
     */
    @Test
    public void simpleRetryDefaultValue(){
        RetryTemplate retryTemplate = new RetryTemplate();

        // 设置重试次数
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(3));

        // 设置退避策略
        ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
        exponentialBackOffPolicy.setInitialInterval(1000);
        exponentialBackOffPolicy.setMultiplier(1.5);
        exponentialBackOffPolicy.setMaxInterval(15000);
        retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);

        StopWatch watch = new StopWatch();watch.start();
        // 执行外部请求
        AtomicInteger counter = new AtomicInteger(0);
        Double execute = retryTemplate.execute(retryContext -> {
            // 重试次数为 3 次 , 表示这里的逻辑第三次必须成功, 否则认定为失败
            Throwable lastThrowable = retryContext.getLastThrowable();
            if (lastThrowable != null) {
                log.info("执行失败,上次失败原因为: {},当前耗时:{} ms", lastThrowable.getMessage(), watch.getTime());
            }
            int andIncrement = counter.getAndIncrement();
            // 当这里改为  < 3 时 , 一定会进 recover 函数
            if (andIncrement < 3) {
                throw new IllegalArgumentException(counter + "");
            }
            return 100 * Math.random();
        },recover -> {
            return 1000D;
        });
        log.info("执行成功,结果为 {} , 耗时: {} ms",execute,watch.getTime());
    }

    @Test
    public  void retryExample3() throws Exception {
        RetryTemplate retryTemplate = new RetryTemplate();

        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(3);

        retryTemplate.setRetryPolicy(simpleRetryPolicy);

        Integer result = retryTemplate.execute(new RetryCallback<Integer, Exception>() {
            int i = 0;

            // 重试操作
            @Override
            public Integer doWithRetry(RetryContext retryContext) throws Exception {
                log.info("retry count: {}", retryContext.getRetryCount()+"");
                return len(i++);
            }
        }, new RecoveryCallback<Integer>() { //兜底回调
            @Override
            public Integer recover(RetryContext retryContext) throws Exception {
                log.info("after retry: {}, recovery method called!", retryContext.getRetryCount());
                return Integer.MAX_VALUE;
            }
        });
        log.info("final result: {}", result);
    }

    private int len(int i) throws Exception {
        if (i < 10) throw new Exception(i + " le 10");
        return i;
    }

    /**
     * 重试的熔断 , 三次重试后, 后面的重试将直接走默认值
     */
    @Test
   public void retryExample4(){
       RetryTemplate template = new RetryTemplate();
       CircuitBreakerRetryPolicy retryPolicy =
               new CircuitBreakerRetryPolicy(new SimpleRetryPolicy(3));
       retryPolicy.setOpenTimeout(5000);
       retryPolicy.setResetTimeout(20000);
       template.setRetryPolicy(retryPolicy);

       for (int i = 0; i < 10; i++) {
           //Thread.sleep(100);
           try {
               Object key = "circuit";
               boolean isForceRefresh = false;
               RetryState state = new DefaultRetryState(key, isForceRefresh);
               String result = template.execute(new RetryCallback<String, RuntimeException>() {
                   @Override
                   public String doWithRetry(RetryContext context) throws RuntimeException {
                       log.info("retry count: {}", context.getRetryCount());
                       throw new RuntimeException("timeout");
                   }
               }, new RecoveryCallback<String>() {
                   @Override
                   public String recover(RetryContext context) throws Exception {
                       return "default";
                   }
               }, state);
               log.info("result: {}", result);
           } catch (Exception e) {
               System.out.println(e);
           }
       }
   }
}

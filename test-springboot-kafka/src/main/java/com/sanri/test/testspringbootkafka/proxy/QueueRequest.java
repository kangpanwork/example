package com.sanri.test.testspringbootkafka.proxy;

import java.lang.annotation.*;

@Target(value= ElementType.METHOD)
@Retention(value= RetentionPolicy.RUNTIME)
@Documented
public @interface QueueRequest {
    /**
     * 请求的主题
     * @return
     */
    String request() default "";

    /**
     * 目标响应的主题
     * @return
     */
    String response() default "";

    String match() default "defaultMessageMatch";
}

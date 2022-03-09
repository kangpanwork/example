package com.sanri.test.testspringbootkafka.proxy;

import java.lang.annotation.*;

@Target(value= ElementType.TYPE)
@Retention(value= RetentionPolicy.RUNTIME)
@Documented
public @interface QueueClient {

}

package com.sanri.test.testspring.customscanproxy.beansdef;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Request {
    String url() default "localhost:8080";
}

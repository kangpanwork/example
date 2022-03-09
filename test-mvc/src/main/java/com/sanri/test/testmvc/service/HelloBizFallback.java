package com.sanri.test.testmvc.service;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(Ordered.LOWEST_PRECEDENCE)
@Component("helloBizFallBack")
public class HelloBizFallback implements HelloBiz {
    @Override
    public boolean support(String company) {
        return true;
    }

    @Override
    public String hello(String msg) {
        return "not support company ";
    }
}

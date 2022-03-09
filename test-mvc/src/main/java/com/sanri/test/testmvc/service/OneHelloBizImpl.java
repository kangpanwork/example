package com.sanri.test.testmvc.service;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class OneHelloBizImpl implements HelloBiz {
    @Override
    public boolean support(String company) {
        return "ha".equals(company);
    }

    @Override
    public String hello(String msg) {
        return "current impl is "+ OneHelloBizImpl.class.getSimpleName();
    }
}

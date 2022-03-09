package com.sanri.test.testmvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class TwoHelloBizImpl implements HelloBiz {
    @Autowired
    private UserService userService;

    @Override
    public boolean support(String company) {
        return "gsc".equals(company);
    }

    @Override
    public String hello(String msg) {
        System.out.println(userService);
        return "current impl is "+TwoHelloBizImpl.class.getSimpleName();
    }
}

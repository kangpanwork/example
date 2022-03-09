package com.sanri.test.testspring.customscanproxy.beansdef;

import org.springframework.web.bind.annotation.GetMapping;

@Request
public interface XXRequstMain {

    @GetMapping("/hello")
    public String hello(String msg);
}

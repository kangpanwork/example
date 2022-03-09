package com.sanri.test.sentinel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping("/sample")
    public String sample(){
        System.out.println("访问接口");
        return "ok";
    }
}

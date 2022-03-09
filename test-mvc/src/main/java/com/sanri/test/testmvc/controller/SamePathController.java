package com.sanri.test.testmvc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SamePathController {

    @RequestMapping("/a/b")
    public void opone(){
        System.out.println("进入方法1 ");
    }

    @RequestMapping(value = "/a/b",method = RequestMethod.POST)
    public void optwo(){
        System.out.println("进入方法2 ");
    }
}

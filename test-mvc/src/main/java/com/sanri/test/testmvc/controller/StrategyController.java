package com.sanri.test.testmvc.controller;

import com.sanri.test.testmvc.service.HelloBiz;
import com.sanri.test.testmvc.service.HelloBizDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
//@Scope("request")
public class StrategyController {
    @Autowired
    private HelloBizDelegate helloBizDelegate;

    @Autowired
    private Map<String,HelloBiz> helloBizMap ;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/stragegy/method")
    public String method(String msg){
        System.out.println(request);
        String abc = request.getParameter("abc");

        System.out.println(this);
        System.out.println();
        return helloBizDelegate.hello("test");
    }

    @GetMapping("/stragegy/invokeParamMethod")
    public String invokeParamMethod(HelloBiz helloBiz,String msg){
        System.out.println(this);
        return helloBiz.hello(msg);
    }
}

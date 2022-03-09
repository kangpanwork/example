package com.sanri.test.web.servlet3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 测试 servlet3 的异步处理能力
 * 模拟 nacos 的长轮询
 */
@RestController
@RequestMapping("/async")
public class AsyncController {

    @Autowired
    private LongPollingService longPollingService;

    @GetMapping("/listener")
    public void lisener(HttpServletRequest request, HttpServletResponse response){
        longPollingService.newLongPollClient(request);
    }
}

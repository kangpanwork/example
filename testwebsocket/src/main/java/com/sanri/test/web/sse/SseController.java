package com.sanri.test.web.sse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
public class SseController {

    @Autowired
    private SseService sseService;

    /**
     * 客户端发起监听请求
     * @return
     */
    @GetMapping("/listener")
    public SseEmitter listener(String id){
        return sseService.accept(id);
    }
}

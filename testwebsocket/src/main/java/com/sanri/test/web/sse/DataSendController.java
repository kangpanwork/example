package com.sanri.test.web.sse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/sse")
public class DataSendController {
    @Autowired
    private SseService sseService;

    /**
     * 向某个客户端发送数据
     * @param id
     * @param content
     */
    @GetMapping("/send")
    public void send(String id,String content) throws IOException {
        sseService.send(id,content);
    }

    /**
     * 结束某个数据推送
     * @param id
     */
    @GetMapping("/complete")
    public void complete(String id){
        sseService.complete(id);
    }
}

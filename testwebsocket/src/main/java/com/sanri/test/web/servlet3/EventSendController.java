package com.sanri.test.web.servlet3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 这个用于发送数据变更事件
 */
@RestController
@RequestMapping("/publish")
public class EventSendController {

    @Autowired
    ApplicationContext applicationContext;

    @GetMapping("/dataChangeEvent")
    public void dataChangeEvent(String groupId,String dataId,String content){
        applicationContext.publishEvent(new ConfigChangeEvent(applicationContext,groupId,dataId));
    }

}

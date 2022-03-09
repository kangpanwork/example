package com.sanri.test.testrabbitmq.consumer;

import com.sanri.test.testrabbitmq.dto.SendBean;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "common_queue_2")
public class OtherConsumer {

    @RabbitHandler
    public void handler(@Payload SendBean message){
        System.out.println("收到消息: "+message);
    }
}

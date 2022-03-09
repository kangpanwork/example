package com.sanri.test.testredis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class MyChannelMessageListener  {

//    public void onMessage(Message message, byte[] pattern) {
//        byte[] channel = message.getChannel();
//        byte[] body = message.getBody();
//
//        System.out.println(new String(channel));
//        System.out.println(new String(body));
//    }

    public void onMessage(String message){
        System.out.println(message);
    }
}

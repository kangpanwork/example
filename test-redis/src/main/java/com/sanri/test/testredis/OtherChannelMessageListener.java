package com.sanri.test.testredis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class OtherChannelMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] rawChannel = message.getChannel();
        byte[] rawBody = message.getBody();

        System.out.println(new String(rawChannel)+":"+new String(rawBody));
    }
}

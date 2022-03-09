package com.sanri.test.rocketmq.consumers;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "TopicTest", consumerGroup = "my-consumer_test-topic-2")
public class TopicTestConsumer  implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("收到消息 : "+ message);
    }
}

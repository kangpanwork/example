package com.sanri.test.rocketmq.consumers;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * 顺序消息消费
 * 这里边指定了  consumeMode = ConsumeMode.ORDERLY, 默认值是  consumeMode = ConsumeMode.CONCURRENT
 */
@Service
@RocketMQMessageListener(topic = "TopicTestOrderly", consumerGroup = "my-consumer_test-topic-2",consumeMode = ConsumeMode.ORDERLY)
public class OrderlyConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }
}

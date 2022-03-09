package com.sanri.test.rocketmq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProduceMessage {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 同步发送, 需要等待发送到 broker 成功才返回, 包含重试时间
     * 默认重试 2 次
     */
    @Test
    public void testProduceSimple(){
        rocketMQTemplate.send(MessageBuilder.withPayload("同步发送,是指什么意思").build());
    }

    /**
     * 测试消息发送(异步)
     * 有回调,发出去 , 异步通知发送结果 , 也会重试 2 次
     */
    @Test
    public void testProduce(){
        SendResult sendResult = rocketMQTemplate.syncSend("TopicTest", "payload 可以是字符串");
        System.out.println(sendResult);

        rocketMQTemplate.asyncSend("TopicTest", "回调结果测试", new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println(sendResult.getMsgId()+" 消息发送成功");
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println(sendResult.getMsgId()+" 消息发送失败");
            }
        });
    }

    /**
     * 单向发送, 发出去就不管了 , 适用于对时间要求高但重要性不强的场景
     */
    @Test
    public void sendOneWay(){
        rocketMQTemplate.sendOneWay("TopicTest","收集到日志信息");
    }

    /**
     * 延时消息
     * messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     */
    @Test
    public void testProduceDelayMessage(){
        Message<String> message = MessageBuilder.withPayload("构建 message 的发送 ").build();
        rocketMQTemplate.syncSend("TopicTest",message,10000,4);

        rocketMQTemplate.syncSend("TopicTest",message,1000,18);
    }

    /**
     * 发送顺序消息
     */
    @Test
    public void testSequenceMessage() throws MQClientException {
        String[] tags = new String[]{"创建订单", "支付", "发货", "收货", "五星好评"};
        for (int i = 5; i < 25; i++) {
            int orderId = i / 5;
            String tag = tags[i % tags.length];
            String message = tag+"_"+orderId;
            rocketMQTemplate.syncSendOrderly("TopicTestOrderly",message,orderId+"");
        }

    }
}

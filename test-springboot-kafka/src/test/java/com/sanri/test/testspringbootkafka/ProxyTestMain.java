package com.sanri.test.testspringbootkafka;

import com.sanri.test.testspringbootkafka.proxy.AuditBiz;
import com.sanri.test.testspringbootkafka.proxy.QueueInvocationHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.lang.reflect.Proxy;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProxyTestMain {
    @Autowired
    private QueueInvocationHandler queueInvocationHandler;

    @Test
    public void test() throws InterruptedException {
        AuditBiz auditBiz = (AuditBiz) Proxy.newProxyInstance(ProxyTestMain.class.getClassLoader(), new Class[]{AuditBiz.class}, queueInvocationHandler);
        ListenableFuture<String> echoMsgFuture = auditBiz.echo("回显消息测试");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        echoMsgFuture.addCallback(new ListenableFutureCallback<String>() {
            @Override
            public void onFailure(Throwable ex) {
                ex.printStackTrace();
                countDownLatch.countDown();
            }

            @Override
            public void onSuccess(String result) {
                System.out.println("回显的消息为:"+result);
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
    }

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Test
    public void send(){
        kafkaTemplate.send("MSG_COMMAND_INBOUND_TOPIC","{\"appID\":\"APP4074\",\"deviceID\":\"102120197085C21CC57D\",\"eventStatus\":3,\"eventTypeID\":90000,\"extAttributes\":{},\"gatewayID\":\"3\",\"isReplyMsg\":0,\"messageID\":\"1310a161c0a741e2820525552f140c95\",\"payload\":{\"attributeList\":[],\"deviceDesc\":\"\",\"deviceID\":\"102120197085C21CC57D\",\"deviceIP\":\"172.24.7.17\",\"deviceModel\":\"ITC-7800A\",\"deviceName\":\"ITC\",\"devicePort\":\"\",\"deviceTypeCode\":\"2019\",\"deviceTypeDesc\":\"广播控制器\",\"extAttributes\":{},\"flag\":0,\"gatewayID\":\"3\",\"macAddress\":\"70-85-C2-1C-C5-7D\",\"mask\":\"\",\"messageID\":\"1310a161c0a741e2820525552f140c95\",\"providerCode\":\"1021\",\"slaveDeviceList\":[],\"softwareVersion\":\"\",\"subDeviceList\":[],\"updateType\":\"UPDATE\"},\"replyFlag\":\"No\",\"timestamp\":\"1599093285025\",\"token\":\"2d30dfa1f412b8bd\",\"topic\":\"MSG_COMMAND_DEVICE\"}");
    }
}

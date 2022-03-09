package com.sanri.test.testspringbootkafka.producers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sanri.test.testspringbootkafka.producers.dto.*;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.AlternativeJdkIdGenerator;
import sun.java2d.SurfaceDataProxy;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class TestScheduleProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;
    private static final String topic = "EGSC_SCP_SDC_EVENT_DATA_TOPIC";

    AlternativeJdkIdGenerator alternativeJdkIdGenerator = new AlternativeJdkIdGenerator();

    public void sendDeleteScheduleCommand(){
        String uuid = alternativeJdkIdGenerator.generateId().toString().replace("-", "");

        String timeformat = DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd'T'HH:mm:ss.S");
        ScreenEvent screenEvent = ScreenEvent.builder()
                .channel(EventChannel.builder().channelId("IOTP").systemId("VGA").build())
                .base(EventBase.builder().eventId(uuid).eventType("20122").eventDesc(RandomUtil.chinese(50,RandomUtil.GIRL)).eventGrade("4").eventTime(timeformat ).build())
                .address(EventAddress.builder().orgName(RandomUtil.username()).addrDesc(RandomUtil.address()).orgType("1").build())
                .device(EventDevice.builder().ctrlDeviceId("10022001123323232323").ctrlDeviceName(RandomUtil.username()).subDeviceName(RandomUtil.username()).build())
                .target(EventTarget.builder().targetId("13").targetName("这是啥").targetType("3").subType("3").build())
                .scene(EventScene.builder().imageUrl(RandomUtil.photoURL()).build())
                .source(EventSource.builder().sourceId("13").sourceName("什么鬼源").build())
                .option("不知道不知道不知道").build();

        EventMsg eventMsg = new EventMsg(screenEvent, "30345");
        kafkaTemplate.send(topic, JSONObject.toJSONString(eventMsg));
    }

    public void batchSend(){
        for (int i = 0; i < 100; i++) {
            sendDeleteScheduleCommand();
        }
    }

    public void sendMsgTomm() throws InterruptedException {
        RandomDataService randomDataService = new RandomDataService();
        while (true){
            Thread.sleep(500);

            Object populateData = randomDataService.populateData(EventDevice.class);
            kafkaTemplate.send("mm-test-9420", JSON.toJSONString(populateData));
        }
    }

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,500,0, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(5000),new ThreadPoolExecutor.CallerRunsPolicy());

    public void sendToAuthTopic() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.submit(new RunThread(kafkaTemplate,countDownLatch));
        }
        countDownLatch.await();
    }

    class  RunThread implements Runnable{
        private  KafkaTemplate kafkaTemplate;
        private CountDownLatch countDownLatch;

        public RunThread(KafkaTemplate kafkaTemplate) {
            this.kafkaTemplate = kafkaTemplate;
        }

        public RunThread(KafkaTemplate kafkaTemplate, CountDownLatch countDownLatch) {
            this.kafkaTemplate = kafkaTemplate;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            for (int j = 0; j < 10000; j++) {
                try {
                    kafkaTemplate.send("EGSC_SCP_UAM_PERSON_AUTH_TOPIC", "{\"traceId\":\"5020809083591065609050\",\"spanId\":\"0\",\"transId\":\"6b87184807314d6999d2186a877e54a1\",\"action\":\"11\",\"msgBody\":{\"doorInfo\":{\"orgType\":\"5\",\"orgName\":\"恒大-海花岛-一号岛-国会-一楼\",\"doorName\":\"国会一层西边大厅2\",\"doorNo\":\"fd793ece966c487f8413e522f34ba357\",\"orgId\":\"f6b0d8adab9146778674485fe72c7f22\"},\"authCore\":{\"authEndTime\":\"2050-01-01 00:00:00\",\"ctrlDeviceId\":\"101520090090C2867B24\",\"timeTemplateId\":\"\",\"credenceNo\":\"004B83AE\",\"subDeviceId\":\"30160090C2867B240001\",\"userId\":\"00906638\",\"validCount\":-1,\"authStartTime\":\"2020-08-17 00:00:00\",\"opTime\":\"2020-08-17 11:30:16\",\"credenceType\":\"2\",\"userType\":\"1\",\"bindDeviceType\":\"2\",\"gatewayId\":\"2\"},\"authRecordOption\":{\"batchNo\":\"20200817_113016_7786\",\"batchSize\":68,\"eventReason\":\"new\"},\"oprInfo\":{},\"authNameInfo\":{\"ctrlDeviceName\":\"国会一层一号弱电井门禁控制器一\",\"subDeviceName\":\"0001 Door\"},\"userOptionInfo\":{\"deptName\":\"恒大海花岛公司案场销售组\",\"deptId\":\"D1208300\",\"employId\":\"00906638\",\"userName\":\"黄路\"}},\"targetSysId\":\"acc\",\"extAttributes\":{}}");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("线程发送成功 ："+Thread.currentThread().getName());
            countDownLatch.countDown();
        }
    }
}

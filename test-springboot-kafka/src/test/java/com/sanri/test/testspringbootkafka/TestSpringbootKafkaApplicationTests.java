package com.sanri.test.testspringbootkafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.sanri.test.testspringbootkafka.producers.TestScheduleProducer;
import com.sanri.test.testspringbootkafka.producers.dto.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSpringbootKafkaApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    KafkaTemplate kafkaTemplate;

    static final String topic = "EGSC_SCP_SDC_EVENT_DATA_TOPIC";

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,10,0, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(1000));

    @Autowired
    KafkaProperties kafkaProperties;

    @Autowired
    AdminClient adminClient;
    @Autowired
    KafkaAdmin kafkaAdmin;

    @Test
    public void createTopic() throws ExecutionException, InterruptedException {
        NewTopic newTopic = new NewTopic("five-abc", 5, (short) 0);
        CreateTopicsResult topics = adminClient.createTopics(Collections.singletonList(newTopic));
    }

    @Test
    public void listTopics() throws ExecutionException, InterruptedException {
        ListTopicsResult listTopicsResult = adminClient.listTopics();
        Set<String> strings = listTopicsResult.names().get();
        System.out.println(strings);
    }

    @Test
    public void testSend(){
        String timeformat = DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd'T'HH:mm:ss.S");
        ScreenEvent screenEvent = ScreenEvent.builder()
                .channel(EventChannel.builder().channelId("IOTP").systemId("ACC").build())
                .base(EventBase.builder().eventId("abc").eventType("1").eventDesc("不知道描述什么，不可描述").eventGrade("3").eventTime(timeformat ).build())
                .address(EventAddress.builder().orgName("机构名称 ").addrDesc("什么鬼地址").orgType("1").build())
                .device(EventDevice.builder().ctrlDeviceName("父设备名称").subDeviceName("子设备名称").build())
                .target(EventTarget.builder().targetId("13").targetName("这是啥").targetType("3").subType("3").build())
                .scene(EventScene.builder().imageUrl("什么鬼图片").build())
                .source(EventSource.builder().sourceId("13").sourceName("什么鬼源").build())
                .option("不知道不知道不知道").build();

        EventMsg eventMsg = new EventMsg(screenEvent, "30345");
        kafkaTemplate.send(topic, JSONObject.toJSONString(eventMsg));
    }

    @Test
    public void testSendBatch(){
        String timeformat = DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd'T'HH:mm:ss.S");
        ScreenEvent screenEvent = ScreenEvent.builder()
                .channel(EventChannel.builder().channelId("IOTP").systemId("ACC").build())
                .base(EventBase.builder().eventId("abc").eventType("1").eventDesc("不知道描述什么，不可描述").eventGrade("3").eventTime(timeformat).build())
                .address(EventAddress.builder().orgName("不知道什么机构").addrDesc("什么鬼地址").orgType("1").build())
                .device(EventDevice.builder().ctrlDeviceName("父设备名称").subDeviceName("子设备名称").build())
                .target(EventTarget.builder().targetId("13").targetName("这是啥").targetType("3").subType("3").build())
                .scene(EventScene.builder().imageUrl("什么鬼图片").build())
                .source(EventSource.builder().sourceId("13").sourceName("什么鬼源").build())
                .option("不知道不知道不知道").build();

        for (int i = 0; i < 1000; i++) {
            EventMsg eventMsg = new EventMsg(screenEvent, "30345");
            threadPoolExecutor.submit(() -> {
                kafkaTemplate.send("abc", JSONObject.toJSONString(eventMsg));
            });

        }

    }



    @Autowired
    TestScheduleProducer scheduleProducer;

    @Test
    public void testSendBatcha(){
        scheduleProducer.batchSend();
    }

    @Autowired
    PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer;

    /**
     * spring 的参数绑定
     */
    @Test
    public void testBind(){
        PropertySources appliedPropertySources = propertySourcesPlaceholderConfigurer.getAppliedPropertySources();
        Iterable<ConfigurationPropertySource> from = ConfigurationPropertySources.from(appliedPropertySources);
        Binder binder = new Binder(from);
        BindResult<KafkaProperties> bind = binder.bind("spring.kafka", KafkaProperties.class);
        KafkaProperties kafkaProperties = bind.get();
        System.out.println(kafkaProperties);
    }

    @Test
    public void testBindCustomYaml() throws IOException {
        YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
        ClassPathResource classPathResource = new ClassPathResource("application-home.yml");
        List<PropertySource<?>> abc = yamlPropertySourceLoader.load("abc", classPathResource);

        Iterable<ConfigurationPropertySource> configurationPropertySources = ConfigurationPropertySources.from(abc);
        Binder binder = new Binder(configurationPropertySources);
        BindResult<KafkaProperties> bind = binder.bind("spring.kafka", KafkaProperties.class);
        KafkaProperties kafkaProperties = bind.get();
        System.out.println(kafkaProperties);
    }

    @Test
    public void yamlDeserializer() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("application-home.yml");
        InputStream inputStream = classPathResource.getInputStream();

        Yaml yaml = new Yaml();
        Object load = yaml.load(inputStream);
        inputStream.close();

        System.out.println(yaml);
        System.out.println(load);
        System.out.println();
    }

    @Test
    public void yamlLoadAs() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("application-home.yml");
        InputStream inputStream = classPathResource.getInputStream();

        Yaml yaml = new Yaml(new Constructor(KafkaProperties.class));
        KafkaProperties kafkaProperties = yaml.loadAs(inputStream, KafkaProperties.class);
        System.out.println(kafkaProperties);
    }

    @Test
    public void bindSerializer(){
        PropertySources appliedPropertySources = propertySourcesPlaceholderConfigurer.getAppliedPropertySources();
        Iterable<ConfigurationPropertySource> from = ConfigurationPropertySources.from(appliedPropertySources);
        Binder binder = new Binder(from);
        BindResult<KafkaProperties> bind = binder.bind("spring.kafka", KafkaProperties.class);
        KafkaProperties kafkaProperties = bind.get();

        Yaml yaml = new Yaml();
        String dump = yaml.dump(kafkaProperties);
        System.out.println(dump);
    }


    @Test
    public void testSendMm() throws InterruptedException {
        scheduleProducer.sendMsgTomm();
    }

    @Test
    public void testToAuth() throws InterruptedException {
        scheduleProducer.sendToAuthTopic();
    }

}

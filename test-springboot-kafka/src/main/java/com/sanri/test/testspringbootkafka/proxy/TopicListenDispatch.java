package com.sanri.test.testspringbootkafka.proxy;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 在启动时会监听所有需要监听的主题,并处理注册的事件
 */
@Component
public class TopicListenDispatch implements InitializingBean {

    @Autowired
    private ListableBeanFactory beanFactory;

//    @Autowired
//    private AdminClient adminClient;

    List<String> listenTopics = new ArrayList<>();

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,5,0, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(16));

    MultiValueMap<String, MatchListenableFuture> responseEventMultiValueMap = new LinkedMultiValueMap();

    @Override
    public void afterPropertiesSet() throws Exception {
        // 获取所有需要监听的主题
        ReflectionUtils.doWithMethods(AuditBiz.class, method -> {
            QueueRequest queueRequest = AnnotationUtils.getAnnotation(method, QueueRequest.class);
            String responseTopic = queueRequest.response();
            listenTopics.add(responseTopic);
        });
        threadPoolExecutor.submit(() -> {
            try {
                startListen();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }
    @Autowired
    KafkaProperties kafkaProperties;

    /**
     * 开始监听需要监听的主题
     */
    private void startListen() throws ExecutionException, InterruptedException {
        Map<String, Object> consumerProperties = kafkaProperties.buildConsumerProperties();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(consumerProperties);

//        DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(listenTopics);
//        Map<String, TopicDescription> topicDescriptionMap = describeTopicsResult.all().get();

        List<TopicPartition> subscribeTopics = new ArrayList<>();
        for (String listenTopic : listenTopics) {
//            TopicDescription topicDescription = topicDescriptionMap.get(listenTopic);
//            List<TopicPartitionInfo> partitions = topicDescription.partitions();
//            for (TopicPartitionInfo partition : partitions) {
//
//            }
            TopicPartition topicPartition = new TopicPartition(listenTopic, 0);
            subscribeTopics.add(topicPartition);
        }
        consumer.assign(subscribeTopics);

       while (true){
           ConsumerRecords<String, String> consumerRecords = consumer.poll(100);
           Iterator<ConsumerRecord<String, String>> consumerRecordIterator = consumerRecords.iterator();
           while (consumerRecordIterator.hasNext()) {
               ConsumerRecord<String, String> consumerRecord = consumerRecordIterator.next();
               String topic = consumerRecord.topic();
               String value = consumerRecord.value();

               // 然后遍历事件,选择一个事件进行处理
               List<MatchListenableFuture> listenableFutures = responseEventMultiValueMap.get(topic);
               for (MatchListenableFuture listenableFuture : listenableFutures) {
                   MessageMatch messageMatch = listenableFuture.getMessageMatch();
                   if(false &&  !messageMatch.match(listenableFuture.getRequestData().getBytes(),value.getBytes())){
                       continue;
                   }
                   listenableFuture.setData(value);
                   threadPoolExecutor.submit(listenableFuture.getListenableFutureTask());
               }
           }
       }
    }

    /**
     * 添加一个事件
     * @param matchListenableFuture
     * @return
     */
    public ListenableFuture addEvent(MatchListenableFuture matchListenableFuture){
        ListenableFutureTask listenableFutureTask = new ListenableFutureTask(() -> {
            String data = matchListenableFuture.getData();
            return data;
        });
        this.responseEventMultiValueMap.add(matchListenableFuture.getTopic(),matchListenableFuture);
        return listenableFutureTask;
    }
}

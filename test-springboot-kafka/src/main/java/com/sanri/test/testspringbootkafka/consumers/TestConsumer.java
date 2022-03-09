package com.sanri.test.testspringbootkafka.consumers;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class TestConsumer {

//    @KafkaListener(topics = "MSG_EVENT_INFOSCREEN",containerFactory = "batchContainerFactory")
//    public void listener(List<ConsumerRecord<String,String>> records, Acknowledgment acknowledgment){
//        if(CollectionUtils.isNotEmpty(records)) {
//            try {
//                for (ConsumerRecord<String, String> record : records) {
//                    String value = record.value();
//                    log.info("value:"+value);
//                }
//            }finally {
//                //在不出异常的情况下提交  offset
//                acknowledgment.acknowledge();
//            }
//        }
//    }

//    @KafkaListener(topics = "abc",containerFactory = "batchContainerFactory")
//    public void listenerMultiThread(List<ConsumerRecord<String,String>> records,Acknowledgment acknowledgment){
//        for (ConsumerRecord<String, String> record : records) {
//            log.info("key:[{}],topic:[{}],partition:[{}],offset:[{}],value:[{}]",record.key(),record.topic(),record.partition(),record.offset(),record.value());
//            acknowledgment.acknowledge();
//        }
//    }

//    @KafkaListener(topics = "abc")
//    public void listenerMultiThread(ConsumerRecord<String,String> record,Acknowledgment acknowledgment){
//        log.info("key:[{}],topic:[{}],partition:[{}],offset:[{}]",record.key(),record.topic(),record.partition(),record.offset());
//        acknowledgment.acknowledge();
//    }

    @KafkaListener(topics = "MSG_COMMAND_INBOUND_TOPIC",groupId = "abc")
    public void listenerMultiThread(ConsumerRecord<String,String> record){
        log.info("key:[{}],topic:[{}],partition:[{}],offset:[{}]",record.key(),record.topic(),record.partition(),record.offset());
    }
}

package com.sanri.test.testspringbootkafka.configs;

import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.listener.concurrency}")
    private int concurrency;

//    @Bean("batchContainerFactory")
//    public KafkaListenerContainerFactory<?> batchContainerFactory(ConsumerFactory consumerFactory) {
//        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory);
//        factory.setConcurrency(concurrency);
//        factory.setBatchListener(true);         //设置为批量消费，每个批次数量在Kafka配置参数中设置ConsumerConfig.MAX_POLL_RECORDS_CONFIG
//        factory.getContainerProperties()
//                .setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL_IMMEDIATE);//设置提交偏移量的方式
//        return factory;
//    }

    @Bean
    public AdminClient adminClient(KafkaProperties kafkaProperties){
        return AdminClient.create(kafkaProperties.buildAdminProperties());
    }
}

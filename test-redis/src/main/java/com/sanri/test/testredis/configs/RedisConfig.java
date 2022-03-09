package com.sanri.test.testredis.configs;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sanri.test.testredis.MyChannelMessageListener;
import com.sanri.test.testredis.OtherChannelMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.concurrent.*;

@Configuration
public class RedisConfig {
    /**
     * 因为消息适配器有 Bean 的后置处理，所以也需要 Spring 容器来管理
     * @param messageListener
     * @return
     */
    @Bean
    public MessageListenerAdapter messageListenerAdapter(MyChannelMessageListener messageListener){
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter();
        messageListenerAdapter.setDefaultListenerMethod("onMessage");
        messageListenerAdapter.setDelegate(messageListener);
        return messageListenerAdapter;
    }

    @Autowired
    OtherChannelMessageListener otherChannelMessageListener;
    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,MessageListenerAdapter messageListenerAdapter ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListenerAdapter, new ChannelTopic("mychannel"));
        container.addMessageListener(otherChannelMessageListener,new ChannelTopic("implchannel"));
        /**
         * 如果不定义线程池，每一次消费都会创建一个线程
         */
        ThreadFactory factory = new ThreadFactoryBuilder()
                .setNameFormat("redis-listener-pool-%d").build();
        Executor executor = new ThreadPoolExecutor(
                1,
                1,
                5L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                factory);
        container.setTaskExecutor(executor);
        container.setSubscriptionExecutor(Executors.newFixedThreadPool(1));
        return container;
    }

    @Bean
    public FastJsonRedisTemplate fastJsonRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        FastJsonRedisTemplate fastJsonRedisTemplate = new FastJsonRedisTemplate();
        fastJsonRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return fastJsonRedisTemplate;
    }

    @Bean
    public KryoRedisTemplate kryoRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        KryoRedisTemplate kryoRedisTemplate = new KryoRedisTemplate();
        kryoRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return kryoRedisTemplate;
    }
}

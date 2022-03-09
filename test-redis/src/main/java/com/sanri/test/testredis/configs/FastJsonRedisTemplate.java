package com.sanri.test.testredis.configs;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * 使用 FastJson 序列化值的 redis 模板
 * 不同序列化的新加一个类，让 RedisTemplate 默认为 JdkSerializer 不影响其他人的使用
 */
public class FastJsonRedisTemplate extends RedisTemplate<String,Object> {
    public FastJsonRedisTemplate() {
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        setKeySerializer(stringRedisSerializer);
        setValueSerializer(fastJsonRedisSerializer);
        setHashKeySerializer(stringRedisSerializer);
        setHashValueSerializer(fastJsonRedisSerializer);
    }

}

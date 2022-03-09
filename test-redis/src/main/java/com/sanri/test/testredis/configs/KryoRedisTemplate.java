package com.sanri.test.testredis.configs;

import com.sanri.test.testredis.configs.serializers.KryoRedisSerializer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class KryoRedisTemplate extends RedisTemplate<String,Object> {
    public KryoRedisTemplate() {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        KryoRedisSerializer kryoRedisSerializer = new KryoRedisSerializer();

        setKeySerializer(stringRedisSerializer);
        setValueSerializer(kryoRedisSerializer);
        setHashKeySerializer(stringRedisSerializer);
        setHashValueSerializer(kryoRedisSerializer);
    }
}

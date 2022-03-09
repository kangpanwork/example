package com.sanri.test.testredis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisCommandTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void opsForValue(){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    }

    @Test
    public void testPipeline(){
        Object execute = redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                for (int i = 0; i < 1000000; i++) {
                    String key = "123" + i;
                    connection.set(key.getBytes(), key.getBytes());
                }
                List<Object> result = connection.closePipeline();
                return result;
            }
        });
        System.out.println(execute);
    }
}

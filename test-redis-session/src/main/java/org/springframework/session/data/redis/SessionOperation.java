package org.springframework.session.data.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.MapSession;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@Component
public class SessionOperation {
    @Autowired
    private RedisOperationsSessionRepository redisOperationsSessionRepository;

    public void loginSuccess(String sessionId){
        RedisOperationsSessionRepository.RedisSession redisSession = redisOperationsSessionRepository.findById(sessionId);
        Field cached = ReflectionUtils.findField(RedisOperationsSessionRepository.RedisSession.class, "cached");
        ReflectionUtils.makeAccessible(cached);
        MapSession mapSession = (MapSession) ReflectionUtils.getField(cached, redisSession);
        mapSession.setId("userId:1");
        redisOperationsSessionRepository.save(redisSession);
    }
}

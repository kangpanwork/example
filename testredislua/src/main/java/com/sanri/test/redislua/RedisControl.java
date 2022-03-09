package com.sanri.test.redislua;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import java.util.Collections;
import java.util.concurrent.TimeUnit;
 
@RestController
public class RedisControl {
 
    @Autowired
    private RedisTemplate redisTemplate;
 
    @Autowired
    DefaultRedisScript<Boolean> redisScript;
 
    @RequestMapping("/redis")
    public String getKey(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        int i=0;
        boolean flag=redisTemplate.opsForValue().setIfAbsent("visitCount_redis","1");
        redisTemplate.expire("visitCount_redis",10,TimeUnit.SECONDS);

        if(flag){
            System.out.println("第一次访问");
        }else{
            valueOperations.increment("visitCount_redis",1);
        }
        i=Integer.valueOf(valueOperations.get("visitCount_redis").toString());
 
        if(i>10){
            return "此接口十秒内访问超过10次，请稍后访问";
        }
 
        return valueOperations.get("visitCount_redis").toString();
    }
 
    @RequestMapping("/redisTest")
    public void redisTest(){
        redisTemplate.opsForList().leftPushAll("l1","a1","a2");
 
        redisTemplate.opsForList().rightPushAll("l2","v1","v2");
 
        BoundListOperations boundListOperations=redisTemplate.boundListOps("l2");
 
        Object r1=boundListOperations.rightPop();
 
        System.out.println(r1);
    }
 
    @RequestMapping("/luaTest")
    public String luaTest(){
        String key = "testredislua";
 
        redisTemplate.delete(key);
        redisTemplate.opsForValue().set(key, "hahaha");
        String s = redisTemplate.opsForValue().get(key).toString();
        System.out.println(s);
 
        redisTemplate.execute(redisScript, Collections.singletonList(key), "hahaha", "3333");
        s = redisTemplate.opsForValue().get(key).toString();
        System.out.println(s);
 
        return null;
    }
 
 
}
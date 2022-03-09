package com.sanri.test.redissession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession(maxInactiveIntervalInSeconds= 1800,redisFlushMode = RedisFlushMode.IMMEDIATE)
public class TestRedisSessionApplication {
	public static void main(String[] args) {
		SpringApplication.run(TestRedisSessionApplication.class, args);
	}
}

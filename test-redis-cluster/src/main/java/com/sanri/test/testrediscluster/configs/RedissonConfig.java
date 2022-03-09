package com.sanri.test.testrediscluster.configs;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RedissonConfig {
    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        List<String> nodes = redisProperties.getCluster().getNodes();
        String[] rLocks=new String[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            rLocks[i]="redis://"+nodes.get(i);
        }
        long millis = redisProperties.getTimeout().toMillis();
        config.useClusterServers().addNodeAddress(rLocks).setTimeout((int) (millis / 1000));
        RedissonClient redissonClient = Redisson.create(config);

        return redissonClient;
    }
}

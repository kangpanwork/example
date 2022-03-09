package com.sanri.test.testredis;

import com.sanri.test.testredis.dto.RedPacketMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedissonMain {
    /**
     * 如何建立一个 redisson
     */
    @Test
    public void testCreate(){
        /*
         * 单机
         */
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.0.134:6379").setDatabase(0);
        RedissonClient redissonClient = Redisson.create(config);
        redissonClient.shutdown();

        /*
         * 主从
         */
        Set<String> slaves = new HashSet<>();
        slaves.add("redis://127.0.0.1:6379");slaves.add("redis://127.0.0.1:6379");
        Config masterSlaveConfig = new Config();
        config.useMasterSlaveServers().setMasterAddress("redis://192.168.0.134:6379")
                .setSlaveAddresses(slaves);
        redissonClient = Redisson.create(config);
        redissonClient.shutdown();

        /*
         * 集群部署方式
         * cluster方式至少6个节点
         * 3主3从，3主做sharding，3从用来保证主宕机后可以高可用
         */
        Config clusterConfig = new Config();
        clusterConfig.useClusterServers().setScanInterval(2000)//集群状态扫描间隔时间，单位是毫秒
                .addNodeAddress("redis://192.168.1.120:6379")
                .addNodeAddress("redis://192.168.1.130:6379")
                .addNodeAddress("redis://192.168.1.140:6379")
                .addNodeAddress("redis://192.168.1.150:6379")
                .addNodeAddress("redis://192.168.1.160:6379")
                .addNodeAddress("redis://192.168.1.170:6379");
        redissonClient = Redisson.create(config);

        /*
         * 哨兵部署方式
         * sentinel是采用 Paxos拜占庭协议，一般sentinel至少3个节点
         */
        config = new Config();
        config.useSentinelServers()
                .setMasterName("my-sentinel-name")
                .addSentinelAddress("redis://192.168.1.120:6379")
                .addSentinelAddress("redis://192.168.1.130:6379")
                .addSentinelAddress("redis://192.168.1.140:6379");
        redissonClient = Redisson.create(config);

        /**
         * 云托管部署方式
         * 这种方式主要解决redis提供商为云服务的提供商的redis连接
         * 比如亚马逊云、微软云
         */
        config.useReplicatedServers()
                //主节点变化扫描间隔时间
                .setScanInterval(2000)
                .addNodeAddress("redis://192.168.1.120:6379")
                .addNodeAddress("redis://192.168.1.130:6379")
                .addNodeAddress("redis://192.168.1.140:6379");
        RedissonClient redisson = Redisson.create(config);
    }

    /**
     * 测试 redisson 的高可用延迟队列
     */
    @Test
    public void testQueue() throws InterruptedException {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.0.134:6379").setDatabase(0);
        RedissonClient redissonClient = Redisson.create(config);

        /**
         * 红包目标队列
         */
        RBlockingQueue<RedPacketMessage> blockingRedPacketQueue
                = redissonClient.getBlockingQueue("redPacketDelayQueue");
        /**
         * 定时任务将到期的元素转移到目标队列
         */
        RDelayedQueue<RedPacketMessage> delayedRedPacketQueue
                = redissonClient.getDelayedQueue(blockingRedPacketQueue);

        /**
         * 延时信息入队列
         */
        delayedRedPacketQueue.offer(new RedPacketMessage(20200113), 3, TimeUnit.HOURS);
        delayedRedPacketQueue.offer(new RedPacketMessage(20200114), 5, TimeUnit.SECONDS);
        delayedRedPacketQueue.offer(new RedPacketMessage(20200115), 10, TimeUnit.SECONDS);
        while (true){
            /**
             * 取出失效红包
             */
            RedPacketMessage redPacket = blockingRedPacketQueue.take();
            log.info("红包ID:{}过期失效",redPacket.getRedPacketId());
            /**
             * 处理相关业务逻辑：记录相关信息并退还剩余红包金额
             */
        }

    }
}

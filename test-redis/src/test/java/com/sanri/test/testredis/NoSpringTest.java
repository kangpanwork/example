package com.sanri.test.testredis;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import redis.clients.jedis.*;

import java.io.*;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class NoSpringTest {
    @Test
    public void testReplay() throws UnsupportedEncodingException {
        Jedis jedis = new Jedis("10.101.72.43",7000,1000);
        jedis.getClient().clusterNodes();
        Client client = jedis.getClient();
//        System.out.println(client.getBulkReply());
        String bulkReply = client.getBulkReply();
        List<String[]> parser = CommandReply.spaceCommandReply.parser(bulkReply);
        for (String[] strings : parser) {
            System.out.println(StringUtils.join(strings,"|"));
        }
        jedis.set("lock","threadName","nx","",60);
        jedis.disconnect();
    }

    @Test
    public void testInfo(){
        Jedis jedis = new Jedis("10.101.72.43",7000,1000);
        String info = jedis.info("Replication");
        System.out.println(info);
        jedis.disconnect();
    }

    @Test
    public void testJedisMulti(){
        String s = UUID.randomUUID().toString();
        System.out.println(s.replace("-",""));
    }

    @Test
    public void testCluster() throws IOException {
        Set<HostAndPort> hostAndPorts = new HashSet<>();
        hostAndPorts.add(new HostAndPort("10.101.72.43",7000));
        hostAndPorts.add(new HostAndPort("10.101.72.43",7001));
        hostAndPorts.add(new HostAndPort("10.101.72.43",7002));

        JedisCluster jedisCluster = new JedisCluster(hostAndPorts);

        String a = jedisCluster.get("a");
        System.out.println(a);

        System.out.println(jedisCluster.type("a"));

        // 不支持 scan key
//        ScanParams scanParams = new ScanParams().match("*auth*").count(1000);
//        ScanResult<String> scan = jedisCluster.scan("0", scanParams);
//        String stringCursor = scan.getStringCursor();
//        List<String> result = scan.getResult();
//        System.out.println(StringUtils.join(result,'\n'));

        jedisCluster.close();
    }

    @Test
    public void testJedis(){
        Jedis jedis = new Jedis("192.168.0.39",6379,1000,60000);
        jedis.auth("hhxredis");

        Long dbSize = jedis.dbSize();
        Long db = jedis.getDB();
        System.out.println(db);

//        String info = jedis.info();
//        System.out.println(info);

        List<String> databases = jedis.configGet("databases");

        System.out.println(databases);
        jedis.close();
    }

    /**
     * 测试用 redis 的 set 数据结构求交集,并集,补集
     */
    @Test
    public void testSet(){
        Set<HostAndPort> hostAndPorts = new HashSet<>();
        hostAndPorts.add(new HostAndPort("10.101.72.43",7000));
        hostAndPorts.add(new HostAndPort("10.101.72.43",7001));
        hostAndPorts.add(new HostAndPort("10.101.72.43",7002));

        JedisCluster jedisCluster = new JedisCluster(hostAndPorts);
        jedisCluster.sadd("{same}mm","1","2","5","9");
        jedisCluster.sadd("{same}9420","1","10","5");

        // 求交集
        Set<String> mm = jedisCluster.sinter("{same}mm", "{same}9420");
        System.out.println(mm);

        // 差集
        Set<String> sdiff = jedisCluster.sdiff("{same}mm", "{same}9420");
        System.out.println(sdiff);

        // 并集
        Set<String> sunion = jedisCluster.sunion("{same}mm", "{same}9420");
        System.out.println(sunion);
    }

    @Test
    public void testZset(){
        Set<HostAndPort> hostAndPorts = new HashSet<>();
        hostAndPorts.add(new HostAndPort("10.101.72.43",7000));
        hostAndPorts.add(new HostAndPort("10.101.72.43",7001));
        hostAndPorts.add(new HostAndPort("10.101.72.43",7002));

        JedisCluster jedisCluster = new JedisCluster(hostAndPorts);
        jedisCluster.zadd("abc",27.5,"abc");
        jedisCluster.zadd("abc",90,"def");
        jedisCluster.zadd("abc",80,"oopq");
        jedisCluster.zadd("abc",38,"fgh");

        List<Tuple> result = new ArrayList<>();
        String cursor = "0" ;
        ScanParams scanParams = new ScanParams();
        scanParams.match("*").count(4);
        do {
            ScanResult<Tuple> zscan = jedisCluster.zscan("abc", cursor,scanParams);
            cursor = zscan.getStringCursor();
            result.addAll(zscan.getResult());
        }while (result.size() < 4);

        for (Tuple tuple : result) {
            String element = tuple.getElement();
            double score = tuple.getScore();
            System.out.println(element+":"+score);
        }
    }
}

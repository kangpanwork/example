package com.sanri.test.testmybatis;

import org.apache.commons.lang3.RandomUtils;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapperParserMain {
    @Test
    public void test() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("BatchMapper.xml");
        InputStream inputStream = classPathResource.getInputStream();
        String resource = classPathResource.getFilename();
        Configuration configuration = new Configuration();
        XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource,new HashMap<>());
        mapperParser.parse();

        MappedStatement mappedStatement = configuration.getMappedStatement("com.sanri.test.testmybatis.mapper.BatchMapper.selectByName");
        Map<String,Object> param = new HashMap<>();
        param.put("ename", "abc");
        BoundSql boundSql = mappedStatement.getBoundSql(param);
        String sql = boundSql.getSql();
        System.out.println(mappedStatement.getBoundSql("dasfasdf").getSql());
        System.out.println(sql);
    }

    @Test
    public void dropRedisKeys(){
        Jedis jedis = new Jedis("192.168.0.134");
        Set<String> keys = jedis.keys("*");
        for (String key : keys) {
            String type = jedis.type(key);
            if ("string".equals(type)){
                jedis.del(key);
            }
        }
    }

    @Test
    public void insertSetData(){
        String keyA = "friend:a";
        String keyB = "friend:b";
        Jedis jedis = new Jedis("192.168.0.134");

//        jedis.sadd(keyA,"b","c","f");
//        jedis.sadd(keyB,"d","f","g");

        Set<String> sinter = jedis.sinter(keyA, keyB);
        System.out.println(sinter);

        Set<String> sdiff = jedis.sdiff(keyA, keyB);
        System.out.println(sdiff);

        Set<String> sunion = jedis.sunion(keyA, keyB);
        System.out.println(sunion);
    }

    @Test
    public void insertStringData(){
        Jedis jedis = new Jedis("192.168.0.134");
        String  [] systems = {"mct","ir","acc","bc"};
        for (int i = 0; i < 5; i++) {
            String system = systems[RandomUtils.nextInt(0,4)];
            String sub = System.currentTimeMillis()+"";
            String biz = RandomUtil.randomAlphabetic(5);
            String value = RandomUtil.phone();

            jedis.set(system+":"+sub+":"+biz,value);
        }

    }
}

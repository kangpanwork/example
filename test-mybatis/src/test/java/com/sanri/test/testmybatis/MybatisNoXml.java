package com.sanri.test.testmybatis;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MybatisNoXml {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test(){
        RowMapper<Person> personRowMapper = new BeanPropertyRowMapper<Person>(Person.class);
        List<Person> query = jdbcTemplate.query("select PIMCITYID as name,PIMCITYNAME as card from t_pimcity ", personRowMapper);
        Jedis jedis = new Jedis("192.168.0.134",6379);

        for (Person person : query) {
            jedis.hset("city",person.getName(),person.getCard());
            jedis.lpush("citylist", JSON.toJSONString(person));
        }
        jedis.close();
    }

    @Data
    static class Person{
        private String name;
        private String card;
    }
}

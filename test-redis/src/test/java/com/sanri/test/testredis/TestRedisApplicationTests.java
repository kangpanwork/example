package com.sanri.test.testredis;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.sanri.test.testredis.configs.KryoRedisTemplate;
import com.sanri.test.testredis.configs.serializers.KryoRedisSerializer;
import com.sanri.test.testredis.dto.NormalObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;
import java.util.Vector;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedisApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private KryoRedisTemplate kryoRedisTemplate;

    @Test
    public void testInject(){
        System.out.println(redisTemplate);
    }

    @Test
    public void testWho(){
        System.out.println();
    }

    @Test
    public void sendMessage() {
        // 相当于 redis publish 数据
        for (int i = 0; i < 100; i++) {
            stringRedisTemplate.convertAndSend("mychannel", RandomStringUtils.randomAlphabetic(5));
        }
    }

    @Test
    public void testRedis(){
        stringRedisTemplate.opsForValue().set("a","c");

    }

    @Test
    public void testSerializer() throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        URL classPath = new File("D:\\test\\c").toURI().toURL();
        ClassLoader classLoader = new URLClassLoader(new URL[]{classPath},null);
        Class<?> aClass = classLoader.loadClass("com.sanri.core.dto.NormalDto");
        Object normalObject = aClass.newInstance();
        Method setTitle = aClass.getMethod("setTitle", String.class);setTitle.setAccessible(true);
        Method setContent = aClass.getMethod("setContent", String.class);setContent.setAccessible(true);
        setTitle.invoke(normalObject,"标题设备");
        setContent.invoke(normalObject,"这是内容");

        System.out.println(JSON.toJSONString(normalObject));

//        kryoRedisTemplate.opsForValue().set("normal",normalObject);
        redisTemplate.opsForValue().set("normal2",normalObject);
    }

    @Test
    public void testJdkc(){
        for (int i = 0; i < 100; i++) {
            NormalObject abc = new NormalObject(RandomStringUtils.randomAlphabetic(10), RandomUtils.nextInt(),new Date());
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.opsForValue().set(RandomStringUtils.randomAlphabetic(10),abc);
        }

    }

    @Test
    public void testClassLoader(){
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(systemClassLoader);
    }

    @Test
    public void testClassCount() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, MalformedURLException {
        URL classPath = new File("D:\\test\\c").toURI().toURL();
        ClassLoader classLoader = new URLClassLoader(new URL[]{classPath},this.getClass().getClassLoader());
        Class<?> aClass = classLoader.loadClass("com.sanri.core.dto.NormalDto");

        Field classes = ClassLoader.class.getDeclaredField("classes");
        classes.setAccessible(true);
        Vector<Class<?>> vector = (Vector<Class<?>>) classes.get(classLoader);
        for (Class<?> aClass1 : vector) {
            System.out.println(aClass1);
        }
    }

    @Test
    public void testDeSerializer() throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        URL classPath = new File("D:\\test\\c").toURI().toURL();
        ClassLoader classLoader = new URLClassLoader(new URL[]{classPath},this.getClass().getClassLoader());
        Class<?> aClass = classLoader.loadClass("com.sanri.core.dto.NormalDto");

        RedisSerializer keySerializer = kryoRedisTemplate.getKeySerializer();
        KryoRedisSerializer kryoRedisSerializer = new KryoRedisSerializer();
        Object normal = kryoRedisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] normals = connection.get(keySerializer.serialize("normal"));
                Kryo kryo = KryoRedisSerializer.kryoLocal.get();

                Input input = new Input(normals);
                kryo.setClassLoader(classLoader);
                Object objectOrNull = kryo.readClassAndObject(input);

                return objectOrNull;
            }
        });
        System.out.println(JSON.toJSONString(normal));
    }

}

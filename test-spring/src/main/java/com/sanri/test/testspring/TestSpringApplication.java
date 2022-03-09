package com.sanri.test.testspring;

import com.sanri.test.testspring.customscan.beandef.XXMapper;
import com.sanri.test.testspring.customscanproxy.beansdef.XXRequstMain;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TestSpringApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TestSpringApplication.class, args);

        XXMapper bean = applicationContext.getBean(XXMapper.class);
        System.out.println("获取到的 XXMapper 看是否有注入: "+bean);

        XXRequstMain bean1 = applicationContext.getBean(XXRequstMain.class);
        System.out.println("xxRequestMain 是否有注入"+bean1);
        String hello = bean1.hello("啥情况");
        System.out.println("调用 hello 返回结果:"+hello);
    }

}

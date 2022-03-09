package com.sanri.test.testspring.configs;

import com.sanri.test.testspring.customscanproxy.ClassPathRequestScan;
import com.sanri.test.testspring.customscanproxy.RequestScannerConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigBean {
    @Value("${spring.application.name}")
    private String appName;

    @Value("#{person}")
    private Person person;

    @Bean
    public Person person(){
        return new Person("sanri",1);
    }

    @Bean
    public RequestScannerConfigurer requestScannerConfigurer(){
        RequestScannerConfigurer requestScannerConfigurer = new RequestScannerConfigurer();
        requestScannerConfigurer.setBasePackage( "com.sanri.test.testspring.customscanproxy.beansdef");
        return requestScannerConfigurer;
    }
}

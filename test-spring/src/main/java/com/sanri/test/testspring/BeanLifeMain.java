package com.sanri.test.testspring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class BeanLifeMain implements BeanNameAware, BeanFactoryAware, InitializingBean, BeanPostProcessor, DisposableBean , ApplicationContextAware {
    @Autowired
    private BeanFactory beanFactory;

    /**
     * 调用构造函数的时候还是没有 BeanFactory 的
     */
    public BeanLifeMain() {
        System.out.println("构建函数");
        System.out.println(beanFactory == null);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("setBeanFactory");
        System.out.println(this.beanFactory == null);

    }

    @Override
    public void setBeanName(String name) {
        System.out.println("setBeanName: "+name);
        System.out.println(this.beanFactory == null);
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destroy");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization: "+beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization: "+beanName);
        return bean;
    }

    /**
     * 注解优先 , afterPropertiesSet 之后
     */
    @PostConstruct
    public void init(){
        System.out.println("PostConstruct");
    }

    /**
     * 注解优先 ,destory 之后
     */
    @PreDestroy
    public void preDestory(){
        System.out.println("preDestory");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("setApplicationContext");
    }
}

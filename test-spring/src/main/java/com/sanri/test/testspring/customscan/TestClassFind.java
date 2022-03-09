package com.sanri.test.testspring.customscan;

import com.sanri.test.testspring.customscan.beandef.XX;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

/**
 * BeanDefinitionRegistryPostProcessor  继承自 BeanFactoryPostProcessor 用于在 BeanFactory 的后置处理，修改 BeanFactory 中的 beans ，或添加 beans
 * 可借助 ClassPathBeanDefinitionScanner 直接将 beans 扫描到容器
 */
@Component
@Slf4j
public class TestClassFind implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner = new ClassPathBeanDefinitionScanner(registry);
        classPathBeanDefinitionScanner.addIncludeFilter(new AnnotationTypeFilter(XX.class));
        int scan = classPathBeanDefinitionScanner.scan("com.sanri.test.testspring.customscan.beandef");
        log.info("扫描到 {} 个类",scan);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

}

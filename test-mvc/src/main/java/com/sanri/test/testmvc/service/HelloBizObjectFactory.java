package com.sanri.test.testmvc.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HelloBizObjectFactory implements ObjectFactory<HelloBizDelegate> , Serializable {
    private ListableBeanFactory beanFactory;

    public HelloBizObjectFactory(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    private Map<String, HelloBizDelegate> cache =  new ConcurrentHashMap<>();

    @Override
    public HelloBizDelegate getObject() throws BeansException {
        HttpServletRequest request = currentRequestAttributes().getRequest();
        String company = request.getParameter("company");
        Collection<HelloBiz> helloBizs = beanFactory.getBeansOfType(HelloBiz.class).values();
        List<HelloBiz> sortHelloBizs = new ArrayList<>(helloBizs);
        sortHelloBizs.sort(AnnotationAwareOrderComparator.INSTANCE);

        for (HelloBiz helloBiz : sortHelloBizs) {
            if (helloBiz.support(company)) {
                HelloBizDelegate helloBizDelegate = cache.computeIfAbsent(company+"."+HelloBiz.class.getName(), k -> new HelloBizDelegate() {
                    @Override
                    public boolean support(String company) {
                        return true;
                    }
                    @Override
                    public String hello(String msg) {
                        return helloBiz.hello(msg);
                    }
                });

                return helloBizDelegate;
            }
        }

        // 最后才会走到 fallback ,所以程序永远不会到这里来
        return null;
    }

    private static ServletRequestAttributes currentRequestAttributes() {
        RequestAttributes requestAttr = RequestContextHolder.currentRequestAttributes();
        if (!(requestAttr instanceof ServletRequestAttributes)) {
            throw new IllegalStateException("Current request is not a servlet request");
        }
        return (ServletRequestAttributes) requestAttr;
    }
}
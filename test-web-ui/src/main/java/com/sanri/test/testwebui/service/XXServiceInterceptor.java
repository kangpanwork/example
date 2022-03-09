package com.sanri.test.testwebui.service;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.context.Lifecycle;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Map;

@Component
public class XXServiceInterceptor implements MethodInterceptor, BeanFactoryAware, SmartLifecycle {
    private BeanFactory beanFactory;
    private Map<String, XXService> xxServiceMap;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Object target = invocation.getThis();
        //第一个参数必须是 BaseParam 或 BaseParam 的子类或有 getBaseParam 方法
        BaseParam baseParam = null;
        Object[] arguments = invocation.getArguments();
        Assert.state(arguments.length >= 1 ,"第一个参数必须是 BaseParam 或 BaseParam 的子类或有 getBaseParam 方法");
        Object argument = arguments[0];
        if(argument instanceof BaseParam){
            baseParam = (BaseParam) argument;
        }else{
            Method getBaseParam = ReflectionUtils.findMethod(argument.getClass(), "getBaseParam");
            Assert.notNull(getBaseParam,"第一个参数必须是 BaseParam 或 BaseParam 的子类或有 getBaseParam 方法");

            baseParam = (BaseParam) ReflectionUtils.invokeMethod(getBaseParam,argument);
        }

        for (XXService xxService : xxServiceMap.values()) {
            if(xxService.support(baseParam)){
                // 注入目标类
                ((XXServiceAware)target).setXXService(xxService);
                return invocation.proceed();
            }
        }
        throw new IllegalArgumentException("不支持的实现:"+baseParam.getCompany());
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void start() {
        Map<String, XXService> xxServiceMap = BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory) beanFactory, XXService.class);
        this.xxServiceMap = xxServiceMap;
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {

    }

    @Override
    public int getPhase() {
        return 0;
    }
}

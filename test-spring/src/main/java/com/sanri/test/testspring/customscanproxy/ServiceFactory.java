package com.sanri.test.testspring.customscanproxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

@Slf4j
public class ServiceFactory<T> implements FactoryBean<T> {
    private Class<T> interfaceType;

    public ServiceFactory(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
    }

    @Override
    public T getObject() throws Exception {
        InvocationHandler handler = new RequestInvocationHandler();
        T t = (T) Proxy.newProxyInstance(interfaceType.getClassLoader(),
                new Class[]{interfaceType}, handler);
        log.info("创建对象:[{}]",t);
        /**
         * 生成的代理类实际上是 RequestInvocationHandler 的对象,同时实现了 interfaceType 接口,调用任何方法都会转换到 invoke 方法上去
         * 这个 getObject 方法会在启动时就调用,只调用一次,注入目标实例到容器中,所以不会创建多个对象 ， 由父类的 isSingleton 决定单例，默认是 true
         */
        return t;
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceType;
    }
}

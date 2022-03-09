package com.sanri.test.deginmodel.proxy.jdk;

import com.sanri.test.deginmodel.proxy.testcglib.User;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyHandler implements InvocationHandler {
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("获取到 sqlId:"+method);
        System.out.println("获取到执行参数列表："+args[0]);
        System.out.println("解析 spel 表达式，并获取到完整的 sql 语句");
        System.out.println("执行 sql ");
        System.out.println("结果集处理，并返回绑定对象");
        return new User("sanri",1);
    }
}

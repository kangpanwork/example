package com.sanri.test.deginmodel.proxy.jdk;

import com.sanri.test.deginmodel.proxy.testcglib.User;
import com.sanri.test.deginmodel.proxy.testcglib.UserOperator;
import jdk.nashorn.internal.codegen.CompilerConstants;

import java.lang.reflect.Proxy;

public class JdkProxy {
    public static void main(String[] args) {
        UserOperator proxyInstance = (UserOperator)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{UserOperator.class}, new ProxyHandler());
        User sanri = proxyInstance.queryByName("sanri");
        System.out.println(sanri);
    }
}

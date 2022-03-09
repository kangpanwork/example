package com.sanri.test.deginmodel.proxy.testcglib;

import net.sf.cglib.proxy.Enhancer;

public class CglibProxy {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();

        enhancer.setSuperclass(UserOperator.class);
        enhancer.setCallback(new ProxyHandler());

        UserOperator userOperator = (UserOperator) enhancer.create();

        User sanri = userOperator.queryByName("sanri");
        System.out.println(sanri);
    }
}

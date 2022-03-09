package com.sanri.test.deginmodel.proxy.proxyClass;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

import java.util.ArrayList;
import java.util.List;

public class CglibProxyTarget {

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();

        enhancer.setSuperclass(Target.class);
//        enhancer.setCallback(new ProxyHandler());
        List<Callback> callbacks = new ArrayList<Callback>();
        callbacks.add(new ProxyHandler());
        enhancer.setCallbacks(callbacks.toArray(new Callback [callbacks.size()]));

        Target target = (Target) enhancer.create();

        System.out.println(target.queryUserByUserName("sanri"));
    }
}

package com.sanri.test.deginmodel.proxy.proxyClass;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class ProxyHandler implements MethodInterceptor {
    public Object intercept(Object obj, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        // obj 是 被代理的 对象,但如果打印 obj 会使用obj 的 toString 方法,然后 toString 被代理,又调 toString 就会死循环栈溢出
        // 所以这里一般要屏蔽 obj 的 Object 方法
        if(method.getDeclaringClass() == Object.class){
            return method.invoke(obj,objects);
        }

        return "hello world ";
    }
}

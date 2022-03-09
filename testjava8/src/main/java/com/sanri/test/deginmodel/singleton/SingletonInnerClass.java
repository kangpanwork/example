package com.sanri.test.deginmodel.singleton;

/**
 * 使用静态内部类来创建单例,这个是利用到了 jvm 加载类的机制,只有使用的时候才加载
 * 加载这个类的时候再去创建实例
 */
public class SingletonInnerClass {
    private SingletonInnerClass(){}

    private static class Inner {
         static SingletonInnerClass singletonInnerClass = new SingletonInnerClass();
    }

    public static SingletonInnerClass getInstance(){
        return Inner.singletonInnerClass;
    }
}

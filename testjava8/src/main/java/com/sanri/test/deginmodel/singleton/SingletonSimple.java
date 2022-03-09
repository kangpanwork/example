package com.sanri.test.deginmodel.singleton;

/**
 * 最简单的单例模式,但如果对象创建比较耗时的话,会在启动时需要过多的时间
 * 饿汉式
 */
public class SingletonSimple {
    private static final SingletonSimple singletonSimple = new SingletonSimple();

    private SingletonSimple() {
        // 如果这里比较耗时,并用这个对象一般情况下用不到时会比较浪费资源
//        Thread.sleep(10000);      //假设构造这个对象需要 10 秒,可能是一个远程连接,或者是比较大的配置对象
    }

    public static SingletonSimple getInstance(){
        return singletonSimple;
    }
}

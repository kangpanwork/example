package com.sanri.test.deginmodel.singleton;

/**
 * 这个用于改进 SingletonNormal 的不足,只有在实例为 null 的时候才进行同步构建,其它情况的检查是没有加锁的
 * 双重检查懒汉式
 *
 * volatile 加这个关键字原因
 * https://blog.csdn.net/zhanglong_4444/article/details/99721590
 */
public class SingletonNormalExtend {
    private volatile static SingletonNormalExtend singletonNormalExtend;
    private SingletonNormalExtend(){}

    public static SingletonNormalExtend getInstance(){
        if(singletonNormalExtend == null){
            // 有可能 A, B 线程同时进入这里,检测到对象为 null ,所以同步块内还要做一次检测
            synchronized (SingletonNormalExtend.class){
                if(singletonNormalExtend == null){
                    singletonNormalExtend = new SingletonNormalExtend();
                }
            }
        }
        return singletonNormalExtend;
    }
}

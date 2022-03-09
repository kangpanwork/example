package com.sanri.test.deginmodel.singleton;

/**
 * 相对于 SingletonSimple 是在用的时候才加载,需要加同步锁,不然会有线程安全问题
 * 懒汉式
 * 但是这个同步锁只需要用于第一次初始化对象,后面依然会锁每一个线程,导致后面的访问效率低下,同一时刻只能一个线程访问
 */
public class SingletonNormal {
    private static SingletonNormal singletonNormal;

    private SingletonNormal() {
    }

    public static synchronized SingletonNormal getInstance(){
        if(singletonNormal == null){
            singletonNormal = new SingletonNormal();
        }
        return singletonNormal;
    }
}

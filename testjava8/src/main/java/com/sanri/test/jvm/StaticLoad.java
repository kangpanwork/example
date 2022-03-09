package com.sanri.test.jvm;

/**
 * 这个静态块在初始化的时候,如果没有使用到是不会执行的
 */
public class StaticLoad {
    static {
        System.out.println("静态块执行");
    }
}

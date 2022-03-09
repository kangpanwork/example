package com.sanri.test.jvm;

public class Start {
    static {
        System.out.println("main 类中的静态代码块比 main 先执行吗");
    }

    public Start(){
        System.out.println("构造函数不会执行");
    }

    {
        System.out.println("代码块不会执行");
    }

    public static void main(String[] args) {
//        System.out.println(TestLoad.a);
//        System.out.println(TestLoad.a);
//        new TestLoad2();
        new StaticLoad();
    }
    static {
        System.out.println("main 之后的静态代码块");
    }
}

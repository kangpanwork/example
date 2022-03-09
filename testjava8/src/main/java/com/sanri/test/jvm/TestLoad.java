package com.sanri.test.jvm;

public class TestLoad {
//    static {
//        System.out.println(a);
//    }
    final int d;
    public TestLoad(){
        d = 4;
    }
    static int a = 1;
    final int b ;
    {
        b = 2;
    }
    static final int c;
    static {
        c = 3;
        System.out.println(c);
    }
    static {
        System.out.println("什么时候加载");
    }

//    {a =3;}
}

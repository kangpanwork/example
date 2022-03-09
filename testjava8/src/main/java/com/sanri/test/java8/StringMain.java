package com.sanri.test.java8;

import org.junit.Test;

public class StringMain {

    @Test
    public void test1(){
        //代码1
        String sa=new String("Hello world");
        String sb=new String("Hello world");
        System.out.println(sa==sb);  // false

        //代码2
        String sc="Hello world";
        String sd="Hello world";
        System.out.println(sc==sd);  // true
    }

    @Test
    public void test2(){
        //代码1
        String sa = "ab";
        String sb = "cd";
        String sab=sa+sb;
        String s="abcd";
        System.out.println(sab==s); // false

        //代码2
        String sc="ab"+"cd";
        String sd="abcd";
        System.out.println(sc==sd); //true
    }
}

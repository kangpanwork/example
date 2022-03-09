package com.sanri.test.jvm;

import org.junit.Test;

public class StringTest {
    /**
     * 常量池中数据,是相等的
     */
    @Test
    public void test(){
        String a = "abc";
        String b= "abc";

        assert a == b;
    }

    /**
     * new String 后就不相等了
     */
    @Test
    public void test2(){
        String a = "abc";
        String b = new String("abc");

        assert a != b;
        assert a.equals(b);
    }
}

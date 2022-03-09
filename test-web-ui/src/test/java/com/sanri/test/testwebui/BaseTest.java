package com.sanri.test.testwebui;

import org.junit.Test;

public class BaseTest {
    class A {}
    class B extends A{};

    @Test
    public void testA(){
        B b = new B();
        assert b instanceof A;
    }

    @Test
    public void testClass(){
        B b  = new B();
//        assert b.getClass() == A.class;
//        System.out.println(b.getClass() == A.class);
    }
}

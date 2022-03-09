package com.sanri.test.sometest;

public class OuterInnerTest {
    public static void main(String[] args) {
        OuterInnerClass.StaticInner staticInner = new OuterInnerClass.StaticInner();
        OuterInnerClass.Inner inner = new OuterInnerClass().new Inner();

    }
}

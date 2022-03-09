package com.sanri.test.java8;

@FunctionalInterface
public interface Defaulable {
    void method();

    default String notRequired(){
        return "default";
    }

    static void staticMethod(){

    }
}

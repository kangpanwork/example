package com.sanri.test.sometest;

import org.junit.Test;

import java.text.MessageFormat;

public class MessageFormatJava {
    @Test
    public void test1(){
        MessageFormat sanri = new MessageFormat("{0}-{1}");
        System.out.println(sanri.getFormats().length);
    }
}

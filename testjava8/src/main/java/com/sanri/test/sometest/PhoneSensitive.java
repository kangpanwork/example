package com.sanri.test.sometest;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneSensitive {
    @Test
    public void testSensitive(){
//        String phone = "17620411498";
        String phone = "123";
        Pattern pattern = Pattern.compile("(\\d{3})(\\d{4})(.*)");
        Matcher matcher = pattern.matcher(phone);
        boolean b = matcher.find();
        if(b) {
            System.out.println(matcher.group(0));
            System.out.println(matcher.group(2));
            System.out.println(matcher.replaceAll("$1****$3"));
        }
    }
}

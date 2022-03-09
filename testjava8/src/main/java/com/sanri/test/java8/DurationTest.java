package com.sanri.test.java8;

import org.junit.Test;

import java.time.Duration;

/**
 * java8 定义的日期格式
 */
public class DurationTest {
    @Test
    public void testParse(){
//        Duration fromChar1 = Duration.parse("P1DT1H10M10.5S");
//        System.out.println(fromChar1.toDays());
//        System.out.println(fromChar1);

//        Duration fromChar2 = Duration.parse("PT10M");
        Duration duration = Duration.parse("P1dT5h1M30S");
        long seconds = duration.getSeconds();
        System.out.println(seconds);
    }
}

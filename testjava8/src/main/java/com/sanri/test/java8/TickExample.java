package com.sanri.test.java8;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class TickExample {

    public static void main(String... args) throws InterruptedException {
        Clock c = Clock.systemDefaultZone(); //获得一个原始钟表，以格林威治标准时间为准
        System.out.println(c);

        Clock c1 = Clock.tick(c, Duration.ofSeconds(5));//获得一个嘀嗒间隔5秒的tickClock钟表
        System.out.println(c1);

        for (int i = 0; i < 15 ; i++) {
            TimeUnit.MILLISECONDS.sleep(1000);//每隔1秒取样一次
            System.out.println("---");
            System.out.println(c.instant());//原始钟表打印时间戳
            System.out.println(c1.instant()+ " tick钟表");//tickClock钟表打印时间戳
        }
    }
}
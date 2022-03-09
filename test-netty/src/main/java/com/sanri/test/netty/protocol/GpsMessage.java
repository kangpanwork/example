package com.sanri.test.netty.protocol;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@Getter
public class GpsMessage {
    private Date date;
    // 卫星个数
    private int moonSize;
    private String latlng;
    private int speed;
    // 航向
    private int course;

    private GpsMessage(){

    }

    public static GpsMessage parse(byte [] content){
        GpsMessage gpsMessage = new GpsMessage();
        int year = 2000 + content[0];
        int month = content[1];
        int day = content[2];
        int hour = content[3];
        int minute = content[4];
        int second = content[5];
        LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, second);
        gpsMessage.date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        gpsMessage.moonSize = content[6];

        return gpsMessage;
    }
}

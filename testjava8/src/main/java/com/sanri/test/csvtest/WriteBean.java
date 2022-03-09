package com.sanri.test.csvtest;

import com.univocity.parsers.annotations.Parsed;
import lombok.Data;

import java.util.Date;

@Data
public class WriteBean {
    // 播放时长 单位：秒
    @Parsed(index = 0,field = "playDuration")
    private Integer playDuration;


    // 设备编号
    @Parsed(index = 1,field = "deviceId")
    private String deviceId;


    // 素材编号
    @Parsed(index = 2,field = "materialId")
    private String materialId;


    // 统计日期
    @Parsed(index = 3,field = "statisticsTime")
    private Date statisticsTime;


    // 播放次数
    @Parsed(index = 4,field = "playTimes")
    private Integer playTimes;

    public WriteBean() {
    }

    public WriteBean(Integer playDuration, String deviceId, String materialId, Date statisticsTime, Integer playTimes) {
        this.playDuration = playDuration;
        this.deviceId = deviceId;
        this.materialId = materialId;
        this.statisticsTime = statisticsTime;
        this.playTimes = playTimes;
    }
}

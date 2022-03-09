package com.sanri.test.deginmodel.brige;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleData {
    private String scheduleId;
    private String publishId;
    private List<String> devices;
}

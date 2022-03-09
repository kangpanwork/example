package com.sanri.test.json.dto;

import lombok.Data;

@Data
public class DeviceStatusCommon {
    private String deviceID;
    private String parentID;
    private boolean isOnline;
    private String time;
    private String deviceType;
}

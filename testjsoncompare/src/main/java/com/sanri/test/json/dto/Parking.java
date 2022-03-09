package com.sanri.test.json.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 停车场
 * 包含 门禁和 摄像头设备
 */
@Data
public class Parking {
    private String name;
    private List<Device> devices = new ArrayList<>();

    public void addDevice(Device device){
        devices.add(device);
    }
}

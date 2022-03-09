package com.sanri.test.json.dto;

public class Camera implements Device {
    private String imei;
    private String name;
    private int type = 2;

    public Camera() {
    }

    public Camera(String imei, String name) {
        this.imei = imei;
        this.name = name;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getType() {
        return this.type;
    }

    public void setType(int type) {

    }
}

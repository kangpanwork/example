package com.sanri.test.json.dto;

public class DoorAccess implements Device {
    private String imei;
    private String name;
    private String position;
    private int type = 1;

    public DoorAccess() {
    }


    public DoorAccess(String imei, String name, String position) {
        this.imei = imei;
        this.name = name;
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

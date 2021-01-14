package com.example.xinbank;

public class DeviceExist {
    private String deviceid, id;

    public DeviceExist() {
    }

    public DeviceExist(String deviceid, String id) {
        this.deviceid = deviceid;
        this.id = id;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package com.r2starbase.apo11o.coeus;

import java.util.Date;

/**
 * Originally created by apo11o on 2/1/16.
 */
public class DeviceInfo {
    protected String deviceName;
    protected boolean alive;
    protected Date lastAlive;

    public DeviceInfo(String n) {
        this.deviceName = n;
        this.alive = true;
        this.lastAlive = new Date();
    }

    public String getDeviceName() {
        return deviceName;
    }

    public boolean isAlive() {
        return alive;
    }

    public Date getLastAlive() {
        return lastAlive;
    }
}

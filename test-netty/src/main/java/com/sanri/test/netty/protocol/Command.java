package com.sanri.test.netty.protocol;

public interface Command {
    /** 实现定位必需要支持的功能 **/
    byte login = 0x01;
    byte hartbeat = 0x08;
    byte gpsOnline = 0x10;
    byte gpsOffline = 0x11;
    byte status = 0x13;
    byte offlineWifiLBS = 0x17;
    byte timeSync = 0x30;
    byte onlineWifiLBS = 0x69;
    byte paramSetting = 0x57;
}

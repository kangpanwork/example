package com.sanri.test.testmvc.dispatchbiz;

public class AuditEventExtend {
    private String systemId;
    private String eventType;

    public String buildUrl() {
        return new StringBuilder().append("/").append(systemId).append("/").append(eventType).toString();
    }
}

package com.sanri.test.testspringbootkafka.proxy;

import lombok.Data;

/**
 * 发起审核请求信息
 */
@Data
public class AuditRequest {
    public static final String topic = "AUDIT_REQUEST";
    private String messageId;
    private String eventId;
    private String scheduleId;
    private String deviceId;
}

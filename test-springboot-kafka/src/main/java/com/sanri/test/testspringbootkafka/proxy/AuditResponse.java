package com.sanri.test.testspringbootkafka.proxy;

import lombok.Data;

/**
 * 审核的响应结果
 */
@Data
public class AuditResponse {
    public static final String topic = "AUDIT_RESPONSE";
    private String messageId;
    private String eventId;
    private Integer audit;
    private String remark;
}

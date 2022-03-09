package com.sanri.test.testspringbootkafka.proxy;

import org.springframework.util.concurrent.ListenableFuture;

@QueueClient
public interface AuditBiz {

    @QueueRequest(request = "ECHO_REQUEST", response = "ECHO_RESPONSE")
    ListenableFuture<String> echo(String msg);

    @QueueRequest(request = AuditRequest.topic, response = AuditResponse.topic,match = "messageIdMatch")
    ListenableFuture<AuditResponse> postAudit(AuditRequest request);
}

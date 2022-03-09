package com.sanri.test.testspringbootkafka.producers.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class BaseBusinessDto implements Serializable {
    private String traceId;
    private String spanId;
    private String businessId;
    private String sourceSysId;
    private String targetSysId;
    private Map<String, Object> extAttributes = new HashMap();

}
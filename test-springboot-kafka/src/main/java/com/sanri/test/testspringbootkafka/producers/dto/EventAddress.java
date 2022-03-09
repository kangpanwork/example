/**
 * Copyright 2017-2025 Evergrande Group.
 */
package com.sanri.test.testspringbootkafka.producers.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @author 081145310
 * @since 2019年7月10日
 */
@Data
@Builder
public class EventAddress {

  private String orgId;
  
  private String orgType;
  
  private String orgName;
  
  private String addrDesc;
  
  private Double longitude;
  
  private Double latitude;

  @Tolerate
  public EventAddress() {
  }
}

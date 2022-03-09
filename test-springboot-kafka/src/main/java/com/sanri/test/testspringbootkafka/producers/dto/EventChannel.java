/**
 * Copyright 2017-2025 Evergrande Group.
 */
package com.sanri.test.testspringbootkafka.producers.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * TODO
 * @author 081145310
 * @since 2019年8月13日
 */
@Data
@Builder
public class EventChannel {

  private String channelId;
  
  private String systemId;

  @Tolerate
  public EventChannel() {
  }
}

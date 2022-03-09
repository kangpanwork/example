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
 * @since 2019年7月10日
 */
@Data
@Builder
public class EventTarget {

  private String targetType;
  
  private String subType;
  
  private String targetId;
  
  private String targetName;

  @Tolerate
  public EventTarget() {
  }
}

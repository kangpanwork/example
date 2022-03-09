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
public class ScreenEvent{

  private EventChannel channel;
  
  private EventBase base;
  
  private EventAddress address;
  
  private EventDevice device;
  
  private EventTarget target;
  
  private EventSource source;
  
  private EventScene scene;
  
  private Object option;

  private EventHandle eventHandle;

  @Tolerate
  public ScreenEvent() {
  }
}

/**
 * Copyright 2017-2025 Evergrande Group.
 */
package com.sanri.test.testspringbootkafka.producers.dto;

import lombok.Data;
import lombok.Getter;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;

import java.util.Date;
import java.util.Map;

/**
 * @author 081145310
 * @since 2019年7月23日
 */
public class EventMsg extends BaseBusinessDto {

  private EventHeader header;

  private ScreenEvent payload;

  private IdGenerator idGenerator = new AlternativeJdkIdGenerator();

  public EventMsg(ScreenEvent screenEvent,String eventType) {
    header = new EventHeader();
    header.setMsgType("event");
    header.setTimestamp(System.currentTimeMillis());
    String uuid = idGenerator.generateId().toString().replace("-", "");
    header.setMessageId(uuid);
    header.setEventType(eventType);
    payload = screenEvent;
  }

  public EventHeader getHeader() {
    return header;
  }

  public ScreenEvent getPayload() {
    return payload;
  }
}

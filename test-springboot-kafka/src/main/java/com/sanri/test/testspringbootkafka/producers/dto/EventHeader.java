/**
 * Copyright 2017-2025 Evergrande Group.
 */
package com.sanri.test.testspringbootkafka.producers.dto;

/**
 * @author 081145310
 * @since 2019年7月23日
 */
public class EventHeader {

  private String msgType;
  
  private String eventType;
  
  /**消息Id*/
  private String messageId;

  /**消息时间戳, 精确到豪秒*/
  private long timestamp;

  public String getMsgType() {
    return msgType;
  }

  public void setMsgType(String msgType) {
    this.msgType = msgType;
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }
  
  
}

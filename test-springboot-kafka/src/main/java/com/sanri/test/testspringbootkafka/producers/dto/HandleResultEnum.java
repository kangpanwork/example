/**
 * Copyright 2017-2025 Evergrande Group.
 */
package com.sanri.test.testspringbootkafka.producers.dto;


/**
 * 处理结果
 * @author 081145310
 * @since 2019年4月15日
 */
public enum HandleResultEnum {

  /* 未处理 */
  INIT("0", "初始结果，未知"),

  /* 处理成功 */
  SUCCESS("1", "处理成功"),
  
  /* 处理成功 */
  FAIL("2", "处理失败"),


  ;


  private String value;

  private String desc;

  private HandleResultEnum(String value, String desc) {
    this.value = value;
    this.desc = desc;
  }

  public String getValue() {
    return value;
  }

  public String getDesc() {
    return desc;
  }
  public static HandleResultEnum getByValue(String value) {
	    for(HandleResultEnum e: HandleResultEnum.values()) {
	      if(e.getValue().equals(value)) {
	        return e;
	      }
	    }
	    return null;
	  }

}

/**
 * Copyright 2017-2025 Evergrande Group.
 */
package com.sanri.test.testspringbootkafka.producers.dto;

/**
 * 处理状态
 * @author 081145310
 * @since 2019年4月15日
 */
public enum HandleStateEnum {

  /* 未处理 */
  UNDO("0", "未处理"),

  /* 已处理 */
  DONE("1", "已处理"),

  /* 已处理 */
  NOT_NEED("8", "不需要处理"),
  ;


  private String value;

  private String desc;

  private HandleStateEnum(String value, String desc) {
    this.value = value;
    this.desc = desc;
  }

  public String getValue() {
    return value;
  }

  public String getDesc() {
    return desc;
  }
  public static HandleStateEnum getByValue(String value) {
	    for(HandleStateEnum e: HandleStateEnum.values()) {
	      if(e.getValue().equals(value)) {
	        return e;
	      }
	    }
	    return null;
	  }
}

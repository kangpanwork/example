/**
 * Copyright 2017-2025 Evergrande Group.
 */
package com.sanri.test.testspringbootkafka.producers.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.Date;

/**
 * @author 081145310
 * @since 2019年7月26日
 */
@Data
@Builder
public class EventHandle {

	private String handleId;

	private String handleState;

	private String handleResult;

	private String handleRemark;

	private Date handleTime;

	private String handleUserId;

	private String handleUserName;

	@Tolerate
	public EventHandle() {
	}
}

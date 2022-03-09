package com.sanri.test.testwebui.controller;

import com.sanri.web.dto.TreeResponseDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Menu {
	private Integer id;
	private String text;
	private Integer pid;

	public Menu() {
	}

	public Menu(Integer id, String text, Integer pid) {
		this.id = id;
		this.text = text;
		this.pid = pid;
	}
}

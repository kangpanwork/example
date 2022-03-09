package com.sanri.test.testwebui.controller;

import com.sanri.web.dto.RootTreeResponseDto;
import com.sanri.web.dto.TreeResponseDto;

import java.util.ArrayList;
import java.util.List;

public class MenuDto extends RootTreeResponseDto<Menu> {
	public MenuDto(Menu origin) {
		super(origin);
	}

	@Override
	public String getId() {
		return origin.getId()+"";
	}

	@Override
	public String getParentId() {
		return origin.getPid()+"";
	}

	@Override
	public String getLabel() {
		return origin.getText();
	}

	@Override
	public Menu getOrigin() {
		return origin;
	}
}

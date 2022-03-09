package com.sanri.test.shiro.web.v1.dtos;

import lombok.Data;

@Data
public class UserMgDto {
    private Long id;
    private String account;

    public UserMgDto() {
    }

    public UserMgDto(Long id, String account) {
        this.id = id;
        this.account = account;
    }
}

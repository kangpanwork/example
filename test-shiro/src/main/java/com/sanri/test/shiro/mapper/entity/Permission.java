package com.sanri.test.shiro.mapper.entity;

import lombok.Data;

@Data
public class Permission {
    private Long id;
    private String name;

    public Permission() {
    }

    public Permission(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

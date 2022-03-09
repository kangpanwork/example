package com.sanri.test.shiro.mapper.entity;

import lombok.Data;

import java.util.List;

@Data
public class Role {
    private Long id;
    private String name;

    public Role() {
    }

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private List<Permission> permissions;
}

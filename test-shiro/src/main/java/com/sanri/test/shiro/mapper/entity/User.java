package com.sanri.test.shiro.mapper.entity;

import lombok.Data;

import java.util.List;

@Data
public class User {
    private Long id;
    private String account;
    private String password;

    public User() {
    }

    public User(Long id, String account) {
        this.id = id;
        this.account = account;
    }

    public User(Long id, String account, String password) {
        this.id = id;
        this.account = account;
        this.password = password;
    }

    private List<Role> roles;
}

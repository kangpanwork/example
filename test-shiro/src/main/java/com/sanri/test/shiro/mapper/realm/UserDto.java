package com.sanri.test.shiro.mapper.realm;

import com.sanri.test.shiro.mapper.entity.Role;
import com.sanri.test.shiro.mapper.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String account;
    private List<Role> roles;

    public UserDto() {
    }

    public UserDto(User user) {
        this.account = user.getAccount();
    }
}

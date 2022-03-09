package com.sanri.test.shiro.mapper;

import com.sanri.test.shiro.mapper.entity.Permission;
import com.sanri.test.shiro.mapper.entity.Role;
import com.sanri.test.shiro.mapper.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 这个对应真实情况下数据查询
 */
@Repository
public class AuthMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * 获取登录用户
     * @param loginName
     * @return
     */
    public User getByloginName(String loginName){
        if ("admin".equals(loginName)){
            String password = passwordEncoder.encode("admin");

            return new User(1L,"admin",password);
        }else if ("9420".equals(loginName)){
            String password = passwordEncoder.encode("h123");
            return new User(2L,"9420",password);
        }
        return null;
    }

    /**
     * 查询帐号角色, 包含资源权限
     * @param account
     * @return
     */
    public List<Role> getRoles(String account) {
        // 四种资源
        List<Permission> normalPermissions = Arrays.asList(new Permission(1L, "home"),
                new Permission(2L, "pay"), new Permission(3L, "flow"));
        List<Permission> adminPermissions = Arrays.asList(new Permission(4L, "users"));

        // 两个角色
        Role normalRole = new Role(1L,"normal");
        normalRole.setPermissions(normalPermissions);

        Role adminRole = new Role(2L,"admin");
        adminRole.setPermissions(adminPermissions);

        // 给管理员和普通用户分配角色
        if ("admin".equals(account)){
            return Arrays.asList(normalRole,adminRole);
        }else if ("9420".equals(account)){
            return Arrays.asList(normalRole);
        }
        return new ArrayList<>();
    }
}

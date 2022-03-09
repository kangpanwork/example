package com.sanri.test.shiro.mapper.realm;

import com.sanri.test.shiro.mapper.AuthMapper;
import com.sanri.test.shiro.mapper.entity.Permission;
import com.sanri.test.shiro.mapper.entity.Role;
import com.sanri.test.shiro.mapper.entity.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private AuthMapper authMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 登录授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserDto primaryPrincipal = (UserDto) principalCollection.getPrimaryPrincipal();
        String account = primaryPrincipal.getAccount();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        // 查询帐号的角色列表
        List<Role> roleList = authMapper.getRoles(account);

        Set<String> collect = roleList.stream().map(Role::getName).collect(Collectors.toSet());
        authorizationInfo.setRoles(collect);

        Set<String> rolePermList = roleList.stream()
                .map(Role::getPermissions)
                .map(permissions -> {
                    String rolePerms = permissions.stream().map(Permission::getName).collect(Collectors.joining(":"));
                    return rolePerms;
                }).collect(Collectors.toSet());

        authorizationInfo.setStringPermissions(rolePermList);

        return authorizationInfo;
    }

    /**
     * 登录认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String loginName = usernamePasswordToken.getUsername();
        String password = new String(usernamePasswordToken.getPassword());

        User user = authMapper.getByloginName(loginName);
        if (user == null){
            throw new AuthenticationException ("帐号或密码错误:"+loginName);
        }
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if (!matches){
            throw new AuthenticationException ("帐号或密码错误");
        }
        UserDto userDto = new UserDto(user);
        return new SimpleAuthenticationInfo(userDto,password,user.getAccount());
    }
}

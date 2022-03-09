package com.sanri.test.shiro.web.v1.controller;

import com.sanri.test.shiro.mapper.realm.UserDto;
import com.sanri.test.shiro.web.v1.dtos.UserMgDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class LoginController {

    @PostMapping("/login")
    public void login(String account,String password){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(account,password);
        subject.login(usernamePasswordToken);
    }

    @GetMapping("/logout")
    public void logout(String account){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

    @GetMapping("/current")
    public UserDto current(){
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        return (UserDto) principal;
    }
}

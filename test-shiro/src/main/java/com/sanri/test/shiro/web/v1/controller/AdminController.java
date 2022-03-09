package com.sanri.test.shiro.web.v1.controller;

import com.sanri.test.shiro.mapper.realm.UserDto;
import com.sanri.test.shiro.web.v1.dtos.UserMgDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/v1/admin")
public class AdminController {

    @GetMapping("/users")
    public List<UserMgDto> users(){
        return Arrays.asList(new UserMgDto(1L,"admin"),new UserMgDto(2L,"9420"));
    }
}

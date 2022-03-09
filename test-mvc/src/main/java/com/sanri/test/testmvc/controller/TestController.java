package com.sanri.test.testmvc.controller;

import com.sanri.test.testmvc.dto.ExampleMessageDto;
import com.sanri.test.testmvc.dto.QueryParam;
import com.sanri.test.testmvc.dto.SystemUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RequestMapping("/test")
@Slf4j
@RestController
public class TestController {

    @GetMapping("/exampleMessage")
    public ExampleMessageDto exampleMessageDto(QueryParam queryParam){
        log.info(queryParam.toString());
        int[] ints = {1, 2, 3};
        return new ExampleMessageDto("sanri",9,new Date(),ints);
    }

    /**
     * 文件上传,多类型参数
     * @param multipartParam
     */
    @PostMapping("/multipartParam")
    public void multipartParam(MultipartFile file,String username,String password){
        System.out.println(file.getOriginalFilename());
        System.out.println(username);
        System.out.println(password);
    }

    @PostMapping("/postData")
    public void testPostData(QueryParam queryParam){
        log.info(queryParam.toString());
    }

    @GetMapping("/exampleEmptyReturn")
    public void exampleEmptyReturn(){

    }

    /**
     * 使用 spring mvc 参数解析 ,没必要自己去实现 aop
     * 这个联合登录模块 user/login
     * @param systemUser
     */
    @GetMapping("/argParser")
    public void argParser(SystemUser systemUser){
        System.out.println(systemUser);
    }
}

package com.sanri.test.testwebui.controller;

import com.sanri.web.configs.TreeResponse;
import com.sanri.web.helper.TreeResponseDtoHelper;
import com.sanri.web.logmark.SysLogMark;
import com.sanri.web.validation.custom.ApprovedFile;
import com.sanri.web.validation.custom.TextFile;
import org.apache.commons.io.IOUtils;
import org.aspectj.lang.annotation.Around;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Validated
public class TestController {

    @GetMapping("/testNoResponse")
    @SysLogMark
    public void testNoResponse(){

    }

    @GetMapping("/testParamTrim")
    @SysLogMark
    public void testParamTrim(TestParam testParam){
        System.out.println(testParam);
    }

    @PostMapping("/testPostParamTrim")
    @SysLogMark
    public void testPostParamTrim(@Validated TestParam testParam, HttpServletRequest request) throws IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
//        System.out.println(parameterMap.size() > 0 ? parameterMap : 0);
//        String println = IOUtils.toString(request.getInputStream(), "utf-8");
//        System.out.println(println);
        System.out.println(testParam);
    }

    @PostMapping("/testPostParamModelAttr")
    @SysLogMark
    public void testPostParamModelAttr(@ModelAttribute TestParam testParam){
        System.out.println(testParam);
    }

    @PostMapping("/testPostParamJsonTrim")
    @SysLogMark
    public void testPostParamJsonTrim(@Validated @RequestBody TestParam testParam){
        System.out.println(testParam);
    }

    @GetMapping("/email")
    @SysLogMark
    public void email(@Email String email){
        System.out.println(email);
    }

    @PostMapping("/upload")
    @SysLogMark
    public void uploadFile(@RequestParam("file") @ApprovedFile(allowFileTypes = "image/*") MultipartFile multipartFile){
        System.out.println(multipartFile);
    }

    @PostMapping("/checkFileCharset")
    public void checkFileCharset(@RequestParam("file") @TextFile(charsLimit = 1024,charsets = "utf-8") MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        IOUtils.copy(inputStream,new FileOutputStream("d:/test/"+multipartFile.getOriginalFilename()));
        System.out.println("复制成功");
    }

    @GetMapping("/treeShowMenu")
    @TreeResponse(type = MenuDto.class,rootParentId = "-1")
    public List<Menu> treeShowMenu(){
        List<Menu> menus = new ArrayList<>();
        menus.add(new Menu(1,"全国",-1));
        menus.add(new Menu(5,"欧洲",-1));
        menus.add(new Menu(2,"湖南",1));
        menus.add(new Menu(3,"长沙",2));
        menus.add(new Menu(4,"深圳",1));
        return menus;
    }

    @GetMapping("/treeShowMenuMap")
    @TreeResponse(type = MenuDto.class,rootParentId = "-1")
    public Map<String,String> treeShowMenuMap(){
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        return stringStringHashMap;
    }

    @GetMapping("/testArgException")
    public void testArgException(){
        throw new IllegalArgumentException("参数异常");
    }
    @GetMapping("/testStateException")
    public void testStateException(){
        throw new IllegalStateException("参数异常");
    }
    public void testAithException(){
        throw new ArithmeticException("算术异常");
    }
}

package com.sanri.test.testmvc.controller;

import com.sanri.test.testmvc.dto.ContentTypeDto;
import com.sanri.test.testmvc.dto.MultipartDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 测试不同的 httpclient 接口
 */
@RestController
@RequestMapping("/httpclient")
public class ContentTypeController {

    @PostMapping("/postFormData")
    public void postFormData(ContentTypeDto contentTypeDto){
        System.out.println(contentTypeDto);
    }

    @PostMapping("/postJsonData")
    public void postJsonData(@RequestBody ContentTypeDto contentTypeDto){
        System.out.println(contentTypeDto);
    }

    @PostMapping("/header")
    public void header(@RequestHeader("token") String token){
        System.out.println(token);
    }

    @PostMapping("/postMultipart")
    public void postMultipart(MultipartDto multipartDto) throws IOException {
        MultipartFile file = multipartDto.getFile();
        file.transferTo(new File("d:/test/"+multipartDto.getIdcard()));
        String idcard = multipartDto.getIdcard();
        System.out.println(idcard);
    }
}

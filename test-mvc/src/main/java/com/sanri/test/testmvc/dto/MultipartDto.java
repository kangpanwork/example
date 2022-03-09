package com.sanri.test.testmvc.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MultipartDto {
    private MultipartFile file;
    private String idcard;
}

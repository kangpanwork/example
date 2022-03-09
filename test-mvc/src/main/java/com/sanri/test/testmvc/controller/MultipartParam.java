package com.sanri.test.testmvc.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class MultipartParam {
    private MultipartFile file;
    private String username;
    private String password;

    public MultipartParam() {
    }

    /**
     * @param file
     * @param username
     * @param password
     */
    public MultipartParam(MultipartFile file, String username, String password) {
        this.file = file;
        this.username = username;
        this.password = password;
    }

}

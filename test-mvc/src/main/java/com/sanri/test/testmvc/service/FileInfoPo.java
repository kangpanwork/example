package com.sanri.test.testmvc.service;

import lombok.Data;

@Data
public class FileInfoPo {
    private String name;
    private String relativePath;
    private long size;
    private long uploaded;
    private String type;
    private String md5;
}

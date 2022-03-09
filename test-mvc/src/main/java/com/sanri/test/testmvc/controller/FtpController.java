package com.sanri.test.testmvc.controller;

import com.sanri.test.testmvc.config.ftp.FtpClientExtend;
import com.sanri.test.testmvc.config.ftp.FtpClientPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 用于测试 ftp 功能
 */
@RequestMapping("/ftp")
@Slf4j
public class FtpController {

    @Autowired
    private FtpClientPool ftpClientPool;

    @Value("${spring.application.name:test-mvc}")
    private String appName;

    /**
     * 测试上传文件
     * @param multipartFile
     */
    @PostMapping("/uploadFile")
    public void uploadFile(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        String originalFilename = multipartFile.getOriginalFilename();
        InputStream inputStream = multipartFile.getInputStream();
        FtpClientExtend ftpClientExtend = null;
        try {
            log.debug(originalFilename+" 开始上传，开始时间:"+ DateFormatUtils.ISO_DATE_FORMAT.format(System.currentTimeMillis()));
            ftpClientExtend = ftpClientPool.borrowObject();

            String yyyyMMdd = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd");
            String extension = FilenameUtils.getExtension(originalFilename);
            String targetPath =  "/"+appName+"/"+yyyyMMdd+"/"+System.currentTimeMillis()+"."+extension;

            StopWatch stopWatch = new StopWatch();stopWatch.start();
            ftpClientExtend.uploadFile(targetPath,inputStream);
            stopWatch.stop();
            log.info("上传["+originalFilename+"]["+multipartFile.getSize()+"]使用时间:"+stopWatch.getTime() + " ms" );
        }finally {
            if(ftpClientExtend != null) {
                ftpClientPool.returnObject(ftpClientExtend);
            }
        }
    }
}

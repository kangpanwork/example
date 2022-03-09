package com.sanri.test.testmvc.controller;

import com.jcraft.jsch.*;
import com.sanri.test.testmvc.service.FileMetaData;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Properties;

@RequestMapping("/sftp")
@RestController
public class SFtpController {

    @Value("${base.upload.path:d:/test}")
    private String baseUploadPath;
    @Value("${base.file.url:https://localhost:8080}")
    private String baseFileUrl;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${sftp.host:192.168.108.128}")
    private String host;
    @Value("${sftp.port:22}")
    private int port;
    @Value("${sftp.username:sanri}")
    private String username;
    @Value("${sftp.password:h123}")
    private String password;

    /**
     * 首先需要获取文件上传元数据信息
     * @return
     */
    @GetMapping("/fileMetaData")
    public FileMetaData fileMetaData(String fileName,long fileSize) throws URISyntaxException {
        String extension = FilenameUtils.getExtension(fileName);
        String finalFileName = System.currentTimeMillis() + "."+ extension;

        String yyyyMMdd = DateFormatUtils.format(System.currentTimeMillis(),"yyyyMMdd");
        URI relativePath = new URI("/"+appName+"/"+yyyyMMdd+"/"+finalFileName);
        URI absoluteURI = new URI("https://localhost:8080/").resolve(relativePath);

        return new FileMetaData(fileName,finalFileName,fileSize,relativePath.toString(),absoluteURI.toString());
    }

    @RequestMapping("/uploadLocal")
    public void uploadLocal(HttpServletRequest request, String relativePath) throws IOException {
        // 转型为MultipartHttpRequest：
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件：
        MultipartFile multipartFile = multipartRequest.getFile("file");

        File targetFile = new File(relativePath);
        if(!targetFile.getParentFile().exists()){
            targetFile.getParentFile().mkdirs();
        }
        if(!targetFile.exists()){targetFile.createNewFile();}

        FileOutputStream fileOutputStream = new FileOutputStream(targetFile,true);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        IOUtils.copy(multipartFile.getInputStream(),bufferedOutputStream);
        bufferedOutputStream.flush();

        IOUtils.closeQuietly(bufferedOutputStream);
        IOUtils.closeQuietly(fileOutputStream);
    }

    /**
     * 分段上传文件到 sftp
     * 方法: 主要实现是在前端，把文件分成一段段的，然后不断的调用这个方法进行上传
     * 注意:
     * 1. 需要等上一段上传完了，才能传下一段，因为是追加进去的
     */
    @RequestMapping("/upload2Sftp")
    public void upload2Sftp(@RequestParam("file") MultipartFile multipartFile,String relativePath) throws JSchException, SftpException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username,host,port);
        session.setPassword(password);
        Properties config = new Properties();
        // 不验证 HostKey
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        try {
            channel.connect();
        }catch (JSchException e){
            if(session != null){session.disconnect();}
            return ;
        }

        try {
            changeDirectory(relativePath, channel);

            //开始追加文件
            channel.put(multipartFile.getInputStream(), relativePath, ChannelSftp.APPEND);
        }finally {
            try{channel.disconnect();}catch (Exception e){}
            try{session.disconnect();}catch (Exception e){}
            channel.exit();
        }
    }

    private void changeDirectory(String relativePath, ChannelSftp channel) throws SftpException {
        Path path = Paths.get(relativePath);
        Iterator<Path> iterator = path.getParent().iterator();
        StringBuffer root = new StringBuffer("");
        while (iterator.hasNext()) {
            Path partPath = iterator.next();
            root.append(partPath.toString());
            try {
                channel.cd(root.toString());
            } catch (SftpException e) {
                channel.mkdir(root.toString());
            }
        }
    }

    /**
     * 最后一步，进行文件完整性检查
     * @param relativePath
     * @param fileSize
     * @param md5
     * @return
     */
    public boolean validateFile(String relativePath,long fileSize,String md5) throws JSchException, SftpException, IOException {
        // 从 sftp 文件服务器下载文件
        JSch jsch = new JSch();
        Session session = jsch.getSession(username,host,port);
        session.setPassword(password);
        Properties config = new Properties();
        // 不验证 HostKey
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        try {
            channel.connect();
        }catch (JSchException e){
            if(session != null){session.disconnect();}
            return false;
        }

        changeDirectory(relativePath,channel);
        File file = new File(relativePath);
        InputStream inputStream = channel.get(file.getName());

        if(inputStream.available() != fileSize){
            return false;
        }
        // 计算 md5 ，如果不相等，则文件上传失败

        return true;
    }
}

package com.sanri.test.testmvc.controller;

import com.alibaba.fastjson.JSONObject;
import com.sanri.test.testmvc.service.BigFileStorage;
import com.sanri.test.testmvc.service.FileInfoPo;
import com.sanri.test.testmvc.service.FileMetaData;
import com.sanri.test.testmvc.service.FileMetaDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class UploadController {

    @PostMapping("/upload/single")
    public void upload(@RequestParam("file") MultipartFile file){
        System.out.println(file.getOriginalFilename());
    }

    @PostMapping("/upload/multi")
    public void multiUpload(@RequestParam("file") MultipartFile [] files){
        System.out.println(files.length);
    }

    @Autowired
    private BigFileStorage bigFileStorage;
    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;

    /**
     * 加载断点文件列表
     * @return
     */
    @GetMapping("/breakPointFiles")
    public List<FileInfoPo> breakPointFiles(){
        List<FileInfoPo> fileInfoPos = fileMetaDataRepository.breakPointFiles();
       return fileInfoPos;
    }

    /**
     * 获取文件元数据，判断文件是否可以秒传
     * @param originFileName
     * @param fileSize
     * @param md5
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/fileMetaData")
    public FileMetaData fileMetaData(String originFileName, Long fileSize, String md5) throws URISyntaxException, MalformedURLException {
        FileMetaData similarFile = bigFileStorage.checkSimilarFile(originFileName,fileSize, md5);
        if(similarFile != null){
            similarFile.setSecUpload(true);

            // 如果文件名不一致，则创建链接文件
            if(!similarFile.getOriginFileName() .equals(originFileName)) {
                bigFileStorage.createSimilarLink(similarFile);
            }
            return similarFile;
        }

        //获取文件相关信息
        String baseName = FilenameUtils.getBaseName(originFileName);
        String extension = FilenameUtils.getExtension(originFileName);

        String finalFileName = bigFileStorage.rename(baseName, fileSize);
        if(StringUtils.isNotEmpty(extension)){
            finalFileName += ("."+extension);
        }

        URI relativePath = bigFileStorage.relativePath(finalFileName);

        //如果没有相似文件，则要创建记录到数据库中，为后面断点续传做准备
        FileInfoPo fileInfoPo = new FileInfoPo();
        fileInfoPo.setName(originFileName);
        fileInfoPo.setType(extension);
        fileInfoPo.setUploaded(0);
        fileInfoPo.setSize(fileSize);
        fileInfoPo.setRelativePath(relativePath.toString());
        fileInfoPo.setMd5(md5);
        fileMetaDataRepository.insert(fileInfoPo);

        URI absoluteURI = bigFileStorage.absolutePath(relativePath);
        FileMetaData fileMetaData = new FileMetaData(originFileName, finalFileName, fileSize, relativePath.toString(), absoluteURI.toString());
        fileMetaData.setMd5(md5);
        fileMetaData.setFileType(extension);
        return fileMetaData;
    }

    /**
     * 获取当前文件已经上传的大小，用于断点续传
     * @return
     */
    @GetMapping("/filePosition")
    public long filePosition(String relativePath) throws IOException, URISyntaxException {
        return bigFileStorage.filePosition(relativePath);
    }

    /**
     * 上传分段
     * @param multipartFile
     * @return
     */
    @PostMapping("/uploadPart")
    public long uploadPart(@RequestParam("file") MultipartFile multipartFile, String relativePath) throws IOException, URISyntaxException {
        bigFileStorage.uploadPart(multipartFile,relativePath);
        return bigFileStorage.filePosition(relativePath);
    }

    /**
     * 检查文件是否完整
     * @param relativePath
     * @param fileSize
     * @param md5
     * @return
     */
    @GetMapping("/checkIntegrity")
    public void checkIntegrity(String relativePath,Long fileSize,String fileName) throws IOException, URISyntaxException {
        long filePosition = bigFileStorage.filePosition(relativePath);
        Assert.isTrue(filePosition == fileSize ,"大文件上传失败，文件大小不完整 "+filePosition+" != "+fileSize);
        String targetMd5 = bigFileStorage.md5(relativePath);
        FileInfoPo fileInfoPo = fileMetaDataRepository.selectByPrimaryKey(fileName);
        String md5 = fileInfoPo.getMd5();
        Assert.isTrue(targetMd5.equals(md5),"大文件上传失败，文件损坏 "+targetMd5+" != "+md5);
        //如果文件上传成功，更新文件上传大小
        fileMetaDataRepository.updateFilePosition(fileName,filePosition);
    }

    @GetMapping("/fileInfoPos")
    public List<FileInfoPo> fileInfoPos(){
        return fileMetaDataRepository.selectAll();
    }
}

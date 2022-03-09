package com.sanri.test.testmvc.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public interface BigFileStorage {
    /**
     * 文件在服务器上存储的最终文件名
     * @param originFileName
     * @param fileSize
     * @return
     */
    String rename(String originFileName, Long fileSize);

    /**
     * 文件在服务器上的相对文件路径
     * @return
     */
    URI relativePath(String finalFileName) throws  URISyntaxException;

    /**
     * 文件在服务器上的绝对路径
     * 用于映射可访问的真实路径
     * @return
     */
    URI absolutePath(URI relativePath) throws  URISyntaxException;

    /**
     * 是否存在相似文件,比较文件大小和 md5 值，如果存在相似文件，返回相似文件
     * @param fileSize
     * @param md5
     * @return
     */
    FileMetaData checkSimilarFile(String originFileName,long fileSize, String md5);

    /**
     * 创建相似文件的链接文件
     * @param sourcePath
     * @param targetPath
     */
    void createSimilarLink(FileMetaData fileMetaData);

    /**
     * 获取当前文件已经上传大小
     * @param relativePath
     * @return
     */
    long filePosition(String relativePath) throws URISyntaxException, IOException;

    /**
     * 上传分段
     * @param multipartFile
     * @param relativePath
     */
    void uploadPart(MultipartFile multipartFile, String relativePath) throws IOException, URISyntaxException;

    /**
     * 计算文件 md5 值
     * @param relativePath
     * @return
     */
    String md5(String relativePath) throws IOException, URISyntaxException;
}

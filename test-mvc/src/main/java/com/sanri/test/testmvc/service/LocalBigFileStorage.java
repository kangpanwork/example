package com.sanri.test.testmvc.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class LocalBigFileStorage implements BigFileStorage {
    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;

    /**
     * 上传的基准路径，默认取当前路径
     */
    @Value("${sanri.webui.upload.basePath:/tmp/}")
    private String basePath;
    @Value("${spring.application.name:default}")
    private String appName;

    @Override
    public String rename(String originFileName, Long fileSize) {
        return System.currentTimeMillis()+"";
    }

    /**
     * 文件路径为 basePath/appName/yyyyMMdd/finalFileName
     * @param finalFileName
     * @return
     */
    static final String yyyyMMdd = "yyyyMMdd";
    @Override
    public URI relativePath(String finalFileName) throws URISyntaxException {
//        String yyyyMMddDate = DateFormatUtils.format(System.currentTimeMillis(), yyyyMMdd);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(yyyyMMdd);
        String yyyyMMddDate = LocalDate.now().format(dateTimeFormatter);
        return new URI(appName+"/"+ yyyyMMddDate+"/"+ finalFileName);
    }

    @Override
    public URI absolutePath(URI relativePath) throws  URISyntaxException {
        return new URI("file","",basePath,"").resolve(relativePath);
    }

    @Override
    public FileMetaData checkSimilarFile(String originFileName,long fileSize, String md5) {
        FileInfoPo fileInfoPo = fileMetaDataRepository.checkSimilarFile(originFileName, fileSize, md5);
        if(fileInfoPo != null){
            // 将 po 转化为 filemetaData
            FileMetaData fileMetaData = new FileMetaData();
            fileMetaData.setOriginFileName(fileInfoPo.getName());
            fileMetaData.setFileSize(fileInfoPo.getSize());
            fileMetaData.setMd5(fileInfoPo.getMd5());
            fileMetaData.setFileType(fileInfoPo.getType());
            return fileMetaData;
        }

        return null;
    }

    @Override
    public void createSimilarLink(FileMetaData fileMetaData) {
        FileInfoPo fileInfoPo = new FileInfoPo();
        fileInfoPo.setMd5(fileMetaData.getMd5());
        fileInfoPo.setName(fileMetaData.getOriginFileName());
        fileInfoPo.setRelativePath(fileMetaData.getRelativePath());
        fileInfoPo.setSize(fileMetaData.getFileSize());
        fileInfoPo.setUploaded(fileMetaData.getFileSize());
        fileInfoPo.setType(fileMetaData.getFileType());
        fileMetaDataRepository.insert(fileInfoPo);
    }

    @Override
    public long filePosition(String relativePath) throws URISyntaxException, IOException {
        File absoluteFile = resolveStorageFile(relativePath);
        return absoluteFile.length();
    }

    @Override
    public void uploadPart(MultipartFile multipartFile, String relativePath) throws IOException, URISyntaxException {
        File absoluteFile = resolveStorageFile(relativePath);

        FileOutputStream fileOutputStream = new FileOutputStream(absoluteFile,true);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        IOUtils.copy(multipartFile.getInputStream(),bufferedOutputStream);
        bufferedOutputStream.flush();
        bufferedOutputStream.close();

        //文件 part 传送完成后，将文件当前位置记录到库
        fileMetaDataRepository.updateFilePosition(multipartFile.getOriginalFilename(),filePosition(relativePath));
    }

    private File resolveStorageFile(String relativePath) throws URISyntaxException, IOException {
        URI absolutePath = absolutePath(new URI(relativePath));
        File absoluteFile = new File(absolutePath);
        File parentFile = absoluteFile.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        if (!absoluteFile.exists()) {
            absoluteFile.createNewFile();
        }
        return absoluteFile;
    }

    @Override
    public String md5(String relativePath) throws IOException, URISyntaxException {
        File absoluteFile = resolveStorageFile(relativePath);

        //大文件 md5 使用流来分段计算
        FileInputStream fileInputStream = new FileInputStream(absoluteFile);
        String md5Hex = DigestUtils.md5Hex(fileInputStream);
        return md5Hex;
    }
}

package com.sanri.test.testmvc.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileMetaDataRepository {

    private List<FileInfoPo> fileInfoPos = new ArrayList<>();
    private Map<String,FileInfoPo> nameIndex = new HashMap<>();
    private LinkedMultiValueMap<String,FileInfoPo> md5Index = new LinkedMultiValueMap<String,FileInfoPo>();

    /**
     * 检查并创建文件链接
     * @param originFileName
     * @param fileSize
     * @param md5
     * @return
     */
    public FileInfoPo checkSimilarFile(String originFileName, long fileSize, String md5) {
        FileInfoPo fileInfoPo = nameIndex.get(originFileName);
        if(fileInfoPo != null){
            if(fileInfoPo.getSize() != fileSize){
                throw new IllegalArgumentException("存在同名素材，请使用别的名称,检测方式为 fileSize ");
            }
            String storageMd5 = fileInfoPo.getMd5();
            if(!storageMd5.equals(md5)){
                throw new IllegalArgumentException("存在同名素材，请使用别的名称,检测方式为 md5 ");
            }

            // 存在素材，可以直接秒传，如果已经传完了的话
            if(fileInfoPo.getSize() != fileInfoPo.getUploaded()) {
                return null;
            }

            return fileInfoPo;
        }

        //如果不存在同名素材，检查文件 md5 是否存在，如存在，则可以秒传
        List<FileInfoPo> fileInfoPos = md5Index.get(md5);
        if(!CollectionUtils.isEmpty(fileInfoPos)) {
            //检测当前文件是否已经传完，如未传完，不能当成相似文件，做两个文件处理
            for (FileInfoPo infoPo : fileInfoPos) {
                if(infoPo.getSize() == infoPo.getUploaded()){
                    return infoPo;
                }
            }

        }
        return null;
    }

    /**
     * 插入记录
     * @param fileInfoPo
     */
    public void insert(FileInfoPo fileInfoPo) {
        fileInfoPos.add(fileInfoPo);
        nameIndex.put(fileInfoPo.getName(),fileInfoPo);
        md5Index.add(fileInfoPo.getMd5(),fileInfoPo);
    }

    /**
     * 获取断点文件列表
     * @return
     */
    public List<FileInfoPo> breakPointFiles() {
        List<FileInfoPo> fileInfoPos = this.fileInfoPos.stream().filter(fileInfoPo -> fileInfoPo.getSize() != fileInfoPo.getUploaded()).collect(Collectors.toList());
        return fileInfoPos;
    }

    /**
     * 更新文件当前位置
     * @param originalFilename
     * @param filePosition
     */
    public void updateFilePosition(String originalFilename, long filePosition) {
        FileInfoPo fileInfoPo = nameIndex.get(originalFilename);
        fileInfoPo.setUploaded(filePosition);
    }

    /**
     * 真实情况下应该是用记录主键查询，这里暂时用文件名称来查询
     * @param fileName
     * @return
     */
    public FileInfoPo selectByPrimaryKey(String fileName) {
        return nameIndex.get(fileName);
    }

    public List<FileInfoPo> selectAll() {
        return fileInfoPos;
    }
}

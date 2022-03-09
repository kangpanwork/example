package com.sanri.test.testmvc.service;

import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class FileMetaData {
    // 原始文件名和最终文件名
    @NonNull private String originFileName;
    @NonNull private String finalFileName;
    //文件大小
    @NonNull private long fileSize;
    // 文件 md5 值，这个是前端计算的值
    private String md5;
    // 相对路径和绝对路径
    @NonNull private String relativePath;
    @NonNull private String absolutePath;
    // 后端检测文件类型
    private String fileType;

    // 文件是否秒传
    private boolean secUpload;
}

package com.sanri.test.fastdfs;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
//import org.csource.common.MyException;
//import org.csource.fastdfs.*;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Date;

/**
 * 使用这个客户端会更方便，更容易和 springboot 结合
 *  <dependency>
 *       <groupId>com.github.tobato</groupId>
 *       <artifactId>fastdfs-client</artifactId>
 *  </dependency>
 *  目前为了展示其完整过程用的这个包
 *
 *  <groupId>org.csource</groupId>
 *  <artifactId>fastdfs-client-java</artifactId>
 *  <version>1.29-SNAPSHOT</version>
 */
//@Slf4j
//public class FastdfsOriginMain {
//    private StorageClient1 client = null;
//
//    @Before
//    public void init(){
//        try {
//            ClientGlobal.init("fdfs_client.conf");
//
//            TrackerClient trackerClient = new TrackerClient();
//            TrackerServer trackerServer = trackerClient.getTrackerServer();
//            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
//            client = new StorageClient1(trackerServer, storageServer);
//
//        } catch (Exception e) {
//            log.error("fastdfs 初始化异常",e);
//        }
//    }
//
//    /**
//     * 测试上传一个文件
//     */
//    @Test
//    public void testUploadFile() throws IOException, MyException {
//        File file = new File("d:/logs/mongod.log");
//        String extension = FilenameUtils.getExtension(file.getName());
//        @Cleanup FileInputStream fileInputStream = new FileInputStream(file);
//
//        String result = client.upload_appender_file1(null, file.length(), new UploadStream(fileInputStream, file.length()), extension, null);
//        System.out.println(result);
//    }
//
//    /**
//     * 测试下载文件
//     * @throws IOException
//     * @throws MyException
//     */
//    @Test
//    public void testDownFile() throws IOException, MyException {
//        String fdfsRelativePath = "group1/M00/00/00/wKgChl7u_O-EZC9kAAAAADTw1dU724.log";
//        @Cleanup FileOutputStream fileOutputStream = new FileOutputStream("d:/test/EZC9kAAAAADTw1dU724.log");
//        DownloadStream downloadStream = new DownloadStream(fileOutputStream);
//
//        client.download_file1(fdfsRelativePath,downloadStream);
//    }
//
//    @Test
//    public void fileInfo() throws IOException, MyException {
//        String fdfsRelativePath = "group1/M00/00/00/wKgChl7u_O-EZC9kAAAAADTw1dU724.log";        // 这个路径对于 fastdfs 来说就是 file_id
//        FileInfo fileInfo = client.get_file_info1(fdfsRelativePath);
//        if(fileInfo == null){
//            System.out.println("文件不存在");
//            return ;
//        }
//        long crc32 = fileInfo.getCrc32();
//        long fileSize = fileInfo.getFileSize();
//        short fileType = fileInfo.getFileType();
//        Date createTimestamp = fileInfo.getCreateTimestamp();
//        String sourceIpAddr = fileInfo.getSourceIpAddr();
//
//        System.out.println(fileInfo);
//    }
//
//    /**
//     * 追加内容，适用于做断点续传
//     */
//    @Test
//    public void appendContent() throws IOException, MyException {
//        String fdfsRelativePath = "group1/M00/00/00/wKgChl7u_O-EZC9kAAAAADTw1dU724.log";        // 这个路径对于 fastdfs 来说就是 file_id
//        File file = new File("d:/logs/mongod.log");
//        @Cleanup FileInputStream fileInputStream = new FileInputStream(file);
//        UploadStream uploadStream = new UploadStream(fileInputStream, file.length());
//        // 这个返回好像不是追加了多少字节， 返回 0 ，难道是成功
//        int i = client.append_file1(fdfsRelativePath, file.length(), uploadStream);
//        System.out.println(i);
//    }
//}

package com.sanri.test.testmvc.config.ftp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Iterator;

@Slf4j
public class FtpClientExtend {
    private FTPClient ftpClient ;

    public FtpClientExtend(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    /**
     * 列出文件列表
     * @param filePath
     * @return
     * @throws IOException
     */
    public FTPFile[] listFiles(String filePath) throws IOException {
        return ftpClient.listFiles(filePath);
    }

    /**
     * 下载文件
     * @param filePath
     * @return
     */
    public InputStream downloadFile(String filePath) throws IOException {
        return ftpClient.retrieveFileStream(filePath);
    }

    /**
     * 存储文件
     * @param s
     * @param inputStream
     */
    public void uploadFile(String filePath, InputStream inputStream) throws IOException {
        File targetFilePath = new File(filePath);
        Path path = targetFilePath.getParentFile().toPath();
        Iterator<Path> iterator = path.iterator();
        StringBuffer root = new StringBuffer("");
        while (iterator.hasNext()){
            Path next = iterator.next();
            root.append("/").append(next);

            //尝试切入目录
            boolean success = ftpClient.changeWorkingDirectory(root.toString());
            if(!success){
                int mkd = ftpClient.mkd(next.toString());
                ftpClient.changeWorkingDirectory(root.toString());
            }
        }

        ftpClient.enterLocalPassiveMode();
        ftpClient.setControlEncoding("UTF-8");
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        boolean storeFileResult = ftpClient.storeFile(targetFilePath.getName(), inputStream);
        if (storeFileResult) {
            log.debug("上传文件:" + filePath + ",到目录:" + ftpClient.printWorkingDirectory() + " 成功");
        }else{
            log.debug("上传文件:" + filePath + ",到目录:" + ftpClient.printWorkingDirectory() + " 失败");
        }
    }
}

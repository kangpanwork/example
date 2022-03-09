package com.sanri.test.testmvc;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Properties;

@Slf4j
public class Main {

    public class SftpSession implements Closeable {
        private Session session;
        private ChannelSftp channel;

        public Session getSession() {
            return session;
        }

        public ChannelSftp getChannel() {
            return channel;
        }

        public SftpSession invoke() throws JSchException {
            //存储文件到远程服务器
            JSch jsch = new JSch();
            session = jsch.getSession("ftpadmin","10.134.195.12",51000);
            session.setPassword("salt202");
            Properties config = new Properties();
            // 不验证 HostKey
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            channel = (ChannelSftp) session.openChannel("sftp");
            try {
                channel.connect();
            }catch (JSchException e){
                if(session != null){session.disconnect();}
                throw e;
            }
            return this;
        }

        /**
         * 关闭连接
         */
        public void close() {
            try{channel.disconnect();}catch (Exception e){log.error(e.getMessage(),e);}
            try{channel.exit();}catch (Exception e){log.error(e.getMessage(),e);}
            try{session.disconnect();}catch (Exception e){log.error(e.getMessage(),e);}
        }

        /**
         * sftp 切换到指定目录
         * @param relativePath
         * @param channel
         * @throws SftpException
         */
        private void changeDirectory(Path relativePath, ChannelSftp channel,boolean rootPath) throws SftpException {
            Iterator<Path> iterator = relativePath.iterator();
            StringBuffer root = new StringBuffer();
            if(rootPath){root.append("/");}
            while (iterator.hasNext()) {
                Path partPath = iterator.next();
                root.append(partPath.toString()).append("/");
                try {
                    if(rootPath){
                        channel.cd(root.toString());
                    }else {
                        channel.cd(partPath.toString());
                    }
                } catch (SftpException e) {
                    try {
                        channel.mkdir(partPath.toString());
                        channel.cd(partPath.toString());
                    }catch (SftpException e2){
                        log.error("创建目录[{}]出错[{}]",root.toString(),e2.getMessage(),e2);
                        throw e;
                    }
                }
            }
        }

    }

    @Test
    public void test() throws JSchException, FileNotFoundException, SftpException {
        SftpSession invoke = new SftpSession().invoke();
        System.out.println(invoke.channel);
        invoke.channel.put(new FileInputStream("d:/test/test.txt"),"/data/package_dispatch/ftpadmin/test.txt",ChannelSftp.APPEND);
        invoke.close();
    }
}

package com.sanri.test.testmvc.config.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.beans.factory.annotation.Value;

public class FtpClientFactory extends BasePooledObjectFactory<FtpClientExtend> {
    @Value("${ftp.host:localhost}")
    private String host;
    @Value("${ftp.port:21}")
    private int port;
    @Value("${ftp.username:ftpadmin}")
    private String username;
    @Value("${ftp.password:salt202}")
    private String password;

    @Override
    public FtpClientExtend create() throws Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(host,port);
        boolean login = ftpClient.login(username, password);
        if(!login){
            throw new RuntimeException("ftp 登录失败,检查用户名密码是否正确["+host+":"+port+"]["+username+"]["+password+"]");
        }
        return new FtpClientExtend(ftpClient);
    }

    @Override
    public PooledObject<FtpClientExtend> wrap(FtpClientExtend ftpClientExtend) {
        return new DefaultPooledObject(ftpClientExtend);
    }
}

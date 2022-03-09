package com.sanri.test.apachecommons;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.util.SubnetUtils;
import org.junit.Test;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;

/**
 * apache commons net
 * 一个用于处理各种网络协议的包, 我们一般会用到其 FTP 功能
 */
public class Net {

    //     ftp 上传下载, 断点续传 https://www.cnblogs.com/softidea/p/4269541.html
    public static void main(String[] args) throws IOException {
        FTPClient ftpClient = new FTPClient();
        StopWatch stopWatch  = new StopWatch();stopWatch.start();
        ftpClient.connect("localhost",21);
        ftpClient.login("appdeploy","Dev123#");
        boolean b = ftpClient.storeFile("b.png", new FileInputStream("C:\\Users\\091795960\\Desktop/V0147_2G.avi"));
        System.out.println(b+""+stopWatch.getTime()+" ms");
        stopWatch.stop();

    }
    @Test
    public void testlistFiles() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect("localhost");
        boolean login = ftpClient.login("sanri", "123");
        FTPFile[] ftpFiles = ftpClient.listFiles("/a/n/c/");
        for (FTPFile ftpFile : ftpFiles) {
            System.out.println(ftpFile);
        }

        ftpClient.disconnect();
    }

    @Test
    public void testGetFile() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect("localhost");
        boolean login = ftpClient.login("sanri", "123");
        InputStream inputStream = ftpClient.retrieveFileStream("/a/n/c/mm.mp4");
        FileOutputStream fileOutputStream = new FileOutputStream("d:/test/a.mp4");
        IOUtils.copy(inputStream,fileOutputStream);
        fileOutputStream.close();
        ftpClient.disconnect();
    }

    /**
     * 测试文件上传
     * @throws IOException
     */
    @Test
    public void testStoreFile() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect("localhost");
        boolean login = ftpClient.login("sanri", "123");
        if (login){
            File file = new File("C:\\Users\\091795960\\Desktop/168aa273ab7d1a331c0350f460095b26.mp4");

            //循环创建路径，并添加文件
            File target = new File("a/n/c/mm.mp4");
            Path path = target.getParentFile().toPath();
            Iterator<Path> iterator = path.iterator();
            StringBuffer root = new StringBuffer("");
            while (iterator.hasNext()){
                Path next = iterator.next();
                root.append("/").append(next);

                //尝试切入目录
                boolean success = ftpClient.changeWorkingDirectory(root.toString());
                if(!success){
                    ftpClient.makeDirectory(next.toString());
                    ftpClient.changeWorkingDirectory(root.toString());
                }
            }
            System.out.println(ftpClient.printWorkingDirectory());
            ftpClient.storeFile(target.getName(), FileUtils.openInputStream(file));
        }
    }

    /**
     * 子网, ip 相关
     */
    @Test
    public void subnet() {
        String subnet = "192.168.38.1/31";
        SubnetUtils utils = new SubnetUtils(subnet);
        SubnetUtils.SubnetInfo info = utils.getInfo();

        System.out.printf("Subnet Information for %s:\n", subnet);
        System.out.println("--------------------------------------");
        System.out.printf("IP Address:\t\t\t%s\t[%s]\n", info.getAddress(), Integer.toBinaryString(info.asInteger(info.getAddress())));
        System.out.printf("Netmask:\t\t\t%s\t[%s]\n", info.getNetmask(), Integer.toBinaryString(info.asInteger(info.getNetmask())));
        System.out.printf("CIDR Representation:\t\t%s\n\n", info.getCidrSignature());
        System.out.printf("Supplied IP Address:\t\t%s\n\n", info.getAddress());
        System.out.printf("Network Address:\t\t%s\t[%s]\n", info.getNetworkAddress(), Integer.toBinaryString(info.asInteger(info.getNetworkAddress())));
        System.out.printf("Broadcast Address:\t\t%s\t[%s]\n", info.getBroadcastAddress(), Integer.toBinaryString(info.asInteger(info.getBroadcastAddress())));
        System.out.printf("Low Address:\t\t\t%s\t[%s]\n", info.getLowAddress(), Integer.toBinaryString(info.asInteger(info.getLowAddress())));
        System.out.printf("High Address:\t\t\t%s\t[%s]\n", info.getHighAddress(), Integer.toBinaryString(info.asInteger(info.getHighAddress())));
        System.out.printf("Total usable addresses: \t%d\n", Integer.valueOf(info.getAddressCount()));
        System.out.printf("Address List: %s\n\n", Arrays.toString(info.getAllAddresses()));
        System.out.println("全部地址：" + Arrays.toString(info.getAllAddresses()));

    }
}

package com.sanri.test.apachecommons;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.*;
import java.util.List;

import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * apache commons io
 */
public class Io {

	@Test
	public void testFreeSpace() throws IOException {
	    // 2.6 即将删除
		long freeSpaceKb = FileSystemUtils.freeSpaceKb("c:/");
    }

	/**
	 * 
	 * 作者:sanri<br/>
	 * 时间:2016-9-13下午4:51:27<br/>
	 * 功能:读取一个文件形成 List</br/>
	 */
	@Test
	public void readLines() throws FileNotFoundException, IOException{
		List<String> readLines = IOUtils.readLines(new FileReader("D:\\collect\\collect.properties"));
		for (String string : readLines) {
			System.out.println(string);
		}
	}
	
	/**
	 * 
	 * 作者:sanri<br/>
	 * 时间:2016-9-13下午4:56:58<br/>
	 * 功能:复制两个流,比我写的方便多了</br/>
	 */
	public void copy() throws IOException {
        InputStream input = null;
        OutputStream output = null;
		IOUtils.copy(input, output);
	}

    /**
     * 流转字符串
     */
	@Test
	public void streamToString() throws IOException {
        InputStream input = null;
        String s = IOUtils.toString(input, Charset.forName("utf-8"));
        System.out.println(s);
    }

    /**
     * 字符串直接转流
     * @throws IOException
     */
    @Test
    public void stringToStream() throws IOException {
        InputStream inputStream = IOUtils.toInputStream("字符串可以直接转流", "utf-8");
        System.out.println(inputStream);
    }

    /**
     * 一些可以使用的常量
     */
    @Test
    public void someConstants(){
        System.out.println(IOUtils.DIR_SEPARATOR_UNIX);
        System.out.println(IOUtils.DIR_SEPARATOR_WINDOWS);
        System.out.println(IOUtils.LINE_SEPARATOR_UNIX);
        System.out.println(IOUtils.LINE_SEPARATOR_WINDOWS);
        System.out.println(IOUtils.LINE_SEPARATOR);
    }

    @Test
    public void fileUtil() throws IOException {
    	File file = new File("d:/test/a.log");
		String s = FileUtils.readFileToString(file, "utf-8");
		System.out.println(s);

		// 级联删除目录
		FileUtils.deleteDirectory(file);

		String baseName = FilenameUtils.getBaseName("a.txt");
	}
}

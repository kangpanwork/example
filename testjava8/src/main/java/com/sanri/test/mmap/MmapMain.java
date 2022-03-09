package com.sanri.test.mmap;

import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MmapMain {

    /**
     * 映射已经存在的文件
     */
    @Test
    public void mapFile() throws IOException {
        // 这个文件一定要存在
        MappedByteBuffer mappedByteBuffer = Files.map(new File("d:/tmp/sanri-sanritools.log"), FileChannel.MapMode.READ_WRITE);
        CharBuffer charBuffer = mappedByteBuffer.asCharBuffer();
        charBuffer.append("这是啥");
        mappedByteBuffer.force();

        mappedByteBuffer.position(156);
        char aChar = mappedByteBuffer.getChar();
        System.out.println(aChar);
    }

    /**
     * 写入一个固定大小的文件,像 rocketmq 就是这么做的
     * @throws IOException
     */
    @Test
    public void fixedFileSizeMap() throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("d:/test/fixed.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 1 * 1024 * 1024 * 1024);
        mappedByteBuffer.putChar('1');
        mappedByteBuffer.putChar('2');
        mappedByteBuffer.force();
    }

    /**
     * 使用 FileChannel 的  transferFrom() 和 transferTo() 方法实现零拷贝
     */
    @Test
    public void testFileChannelTrans() throws IOException {
        try(
            FileChannel fromChannel = new RandomAccessFile("d:/test/source.txt","rw").getChannel();
            FileChannel toChannel = new RandomAccessFile("d:/test/target.txt","rw").getChannel();
        ){
            long position = 0 ;
            long offset = fromChannel.size();
            fromChannel.transferTo(position,offset,toChannel);
        }
    }

}

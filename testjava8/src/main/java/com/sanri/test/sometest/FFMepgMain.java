package com.sanri.test.sometest;

import it.sauronsoftware.jave.Encoder;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;

public class FFMepgMain {
    public static void main(String[] args) {
        File source = new File("C:\\Users\\091795960\\Videos/HD.Club-4K-Chimei-inn-20mbps.mp4");
        Encoder encoder = new Encoder();
        FileChannel fc= null;
        try {
            it.sauronsoftware.jave.MultimediaInfo encoderInfo = encoder.getInfo(source);
            long duration = encoderInfo.getDuration();
            System.out.println("此视频时长为:"+ DurationFormatUtils.formatDuration(duration,"HH:mm:ss"));
            //视频帧宽高
            System.out.println("此视频高度为:"+encoderInfo.getVideo().getSize().getHeight());
            System.out.println("此视频宽度为:"+encoderInfo.getVideo().getSize().getWidth());
            System.out.println("此视频格式为:"+encoderInfo.getFormat());
            FileInputStream fis = new FileInputStream(source);
            fc= fis.getChannel();
            BigDecimal fileSize = new BigDecimal(fc.size());
            String size = fileSize.divide(new BigDecimal(1048576), 2, RoundingMode.HALF_UP) + "MB";
            System.out.println("此视频大小为"+size);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (null!=fc){
                try {
                    fc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

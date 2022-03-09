package mintest;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileAttr {
    @Test
    public void testAccessTime() throws IOException {
        File file = new File("D:\\home\\appdeploy\\sanri-test\\configs\\connect\\redis");
        File[] files = file.listFiles();
        List<File> list = Arrays.asList(files);
        Collections.sort(list,(aFile,bFile) -> {

            try {
                BasicFileAttributes basicFileAAttributes = Files.readAttributes(aFile.toPath(), BasicFileAttributes.class);
                FileTime aFileaccessTime = basicFileAAttributes.lastAccessTime();

                BasicFileAttributes basicBFileAttributes = Files.readAttributes(bFile.toPath(), BasicFileAttributes.class);
                FileTime bFileaccessTime = basicBFileAttributes.lastAccessTime();

                return bFileaccessTime.compareTo(aFileaccessTime);
            } catch (IOException e) {
                return -1 ;
            }
        });

        System.out.println(list);
    }

    @Test
    public void testBit(){
        String bitBinString = get8BitBinString(254);
        System.out.println(bitBinString);
    }

    private  String get8BitBinString(int number) {
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++){
            sBuilder.append(number & 1);
            number = number >>> 1;
        }
        return sBuilder.reverse().toString();
    }
}

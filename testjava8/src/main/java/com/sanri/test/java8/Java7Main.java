package com.sanri.test.java8;

import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;
import java.util.*;

public class Java7Main {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("a", "b", "c");
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
    }

    @Test
    public void testNumerSeparator(){
        // 支持分隔符
        int a = 11_22_33;
        // 支持二进制数字
        int b= 0b1010;
        System.out.println(a);
        System.out.println(b);
    }

    @Test
    public void testTryResource(){
        // InputStream 实现了 AutoCloseable 接口
        try(InputStream inputStream = new FileInputStream("d:/test")){
            System.out.println("do something");
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Test
   public void testSwitch(){
        String stringSwitch  = "mm";
        switch (stringSwitch){
            case "mm":
                break;
        }
   }

   @Test
   public void testFilePath() throws IOException {
       Path path = Paths.get("a", "b");
       Iterator<Path> iterator = path.iterator();

       List<String> lines = Files.readAllLines(path);

       Files.copy(path,Files.newOutputStream(path));
   }

   @Test
   public void testPath(){
        Path path = Paths.get("/foo", "bar", "bin");
        Path resolve = path.resolve("/mm");
        System.out.println(path.resolve("a"));
   }

   @Test
   public void fileStore() throws IOException {
       FileStore fileStore = Files.getFileStore(Paths.get("c:/"));
       System.out.println("FileAttributeView supported: " + fileStore.supportsFileAttributeView(FileAttributeView.class));
       System.out.println("BasicFileAttributeView supported: "+ fileStore.supportsFileAttributeView(BasicFileAttributeView.class));
       System.out.println("FileOwnerAttributeView supported: "+ fileStore.supportsFileAttributeView(FileOwnerAttributeView.class));
       System.out.println("AclFileAttributeView supported: "+ fileStore.supportsFileAttributeView(AclFileAttributeView.class));
       System.out.println("PosixFileAttributeView supported: "+ fileStore.supportsFileAttributeView(PosixFileAttributeView.class));
       System.out.println("UserDefinedFileAttributeView supported: " + fileStore.supportsFileAttributeView(UserDefinedFileAttributeView.class));
       System.out.println("DosFileAttributeView supported: "+ fileStore.supportsFileAttributeView(DosFileAttributeView.class));

       System.out.println("FileAttributeView supported: "+ fileStore.supportsFileAttributeView("file"));
       System.out.println("BasicFileAttributeView supported: "+ fileStore.supportsFileAttributeView("basic"));
       System.out.println("FileOwnerAttributeView supported: "+ fileStore.supportsFileAttributeView("owner"));
       System.out.println("AclFileAttributeView supported: "+ fileStore.supportsFileAttributeView("acl"));
       System.out.println("PosixFileAttributeView supported: "+ fileStore.supportsFileAttributeView("posix"));
       System.out.println("UserDefinedFileAttributeView supported: "+ fileStore.supportsFileAttributeView("user"));
       System.out.println("DosFileAttributeView supported: "+ fileStore.supportsFileAttributeView("dos"));

   }
}

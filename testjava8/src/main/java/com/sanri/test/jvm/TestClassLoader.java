package com.sanri.test.jvm;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.Vector;

/**
 * 这个用来测试是不是这样进行热更新的
 * 显然类加载后是在堆中有缓存的,不可能重新加载就更新类,热更新在 tomcat 中是重新创建一个类加载器来加载
 * 或者使用 attachment 来加载如 arthas 和 jmap 类的工具,这个我就没有仔细去看了
 */
public class TestClassLoader {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
//        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        File dir = new File("d:/test/abc/com/sanri/app/chat");
        FileUtils.copyFile(new File(dir,"Person1.class"),new File(dir,"com/sanri/app/chat/Person.class"));

        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{dir.toURI().toURL()}, null);
        Class<?> aClass = urlClassLoader.loadClass("com.sanri.app.chat.Person");
        try {
            Field age = aClass.getDeclaredField("age");
            System.out.println(age);
        }catch (NoSuchFieldException e){
            System.out.println("没有有 age 属性");
        }
        FileUtils.copyFile(new File(dir,"Person2.class"),new File(dir,"com/sanri/app/chat/Person.class"));

        // 删除类加载器中的内容
        Field classes = ClassLoader.class.getDeclaredField("classes");classes.setAccessible(true);
        Vector<Class<?>> loadClasses = (Vector<Class<?>>) classes.get(urlClassLoader);
        Iterator<Class<?>> iterator = loadClasses.iterator();
        while (iterator.hasNext()){
            Class<?> next = iterator.next();
            if("com.sanri.app.chat.Person".equals(next.getName())){
                iterator.remove();
            }
        }

        Class<?> newClass = urlClassLoader.loadClass("com.sanri.app.chat.Person");
        try {
            Field age = aClass.getDeclaredField("age");
            System.out.println(age);
        }catch (NoSuchFieldException e){
            System.out.println("没有有 age 属性 后面的");
        }
    }

    @Test
    public void testRefClass() throws ClassNotFoundException, MalformedURLException, NoSuchFieldException, IllegalAccessException {
        URL classPath = new File("D:\\test\\c").toURI().toURL();
        ClassLoader classLoader = new URLClassLoader(new URL[]{classPath},this.getClass().getClassLoader());
        Class<?> aClass = classLoader.loadClass("com.sanri.test.dto.RefDto");
        System.out.println(aClass);

        Field classes = ClassLoader.class.getDeclaredField("classes");
        classes.setAccessible(true);
        Vector<Class<?>> vector = (Vector<Class<?>>) classes.get(classLoader);
        for (Class<?> aClass1 : vector) {
            System.out.println(aClass1);
        }
    }
}

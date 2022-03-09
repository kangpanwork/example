package mintest;

import com.sanri.test.java8.Car;
import org.junit.Test;

import java.lang.reflect.Field;

public class Unbelievable {

    @Test
    public void test() throws NoSuchFieldException, IllegalAccessException { haha();
        Integer a =  1;
        if(a == (Integer)3 ){
            System.out.println("success");
        }
    }

    @Test
    public void testClass(){
        System.out.println(Car [].class);
        System.out.println(Integer.class);
        System.out.println(int.class);
        System.out.println(int[].class);
        System.out.println(byte[].class);
        System.out.println(long [].class);
        System.out.println(short [].class);
        System.out.println(Integer [].class);
    }


    @Test
    public void testError(){
        for (int i = 0; i < 50_000; i++) {
            int a = 8 ;
            while ((a -= 3 )> 0);
            System.out.println("a = "+a);
        }
    }

    public static void test2(){
        int i=8;
        while ((i -= 3 ) > 0) ;
        System.out.println("i = " + i);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            test2();
        }
    }




    private void haha() throws NoSuchFieldException, IllegalAccessException {
        Class cache =  Integer.class.getDeclaredClasses()[0];
        Field c = cache.getDeclaredField("cache");
        c.setAccessible(true);
        Integer [] array = (Integer [])c.get(cache);
        array [130] = array [129];
        array [131] = array [129];
    }
}

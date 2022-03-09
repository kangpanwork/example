package com.sanri.test.java8;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1,2,3,4,5);
        list.stream().map(x->2 * x).forEach(System.out::print);

       byte b1=1;
        byte b2=3;
        int b3=b1+b2;

        System.out.println(b3);
    }

    @Test
    public void testMapMirror(){
        List<String> a = Arrays.asList("a","b","c");
        List<String> b = Arrays.asList("aa","bb","cc");
        Map<String,String> map = Stream.iterate(0, n -> n + 1)
                .limit(Math.min(a.size(),b.size()))
                .collect(Collectors.toMap(i -> a.get(i), i -> b.get(i)));
        System.out.println(map);
    }
}

package com.sanri.test.sometest;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 布隆过滤器测试
 * https://www.jianshu.com/p/2104d11ee0a2
 * 布隆过滤器的特点就是能判断元素一定不存在,但元素是否存在是有误判的
 * 它使用 k 个函数计算 元素 在 bit 数组上的位置,并把其置于 1 ,下次判断元素是否存在时,只要再次计算值,只要其中一个位置为 0 ,则元素一定不存在,反之可能存在
 * 我们需要预估 bit 数组的大小和 函数的个数 ,可以使用公式
 * k = (m/n)ln2 | k 为函数个数 , m 为 bit 数组长度 , n 为插入元素个数
 */
public class BloomFilterMain {
    private static int size = 100_0000;

    private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size);

    /**
     * 测试匹配时间,在 100 万个数中
     */
    @Test
    public void testFirst(){
        for (int i = 0; i < size; i++) {
            bloomFilter.put(i);
        }

        long startTime = System.nanoTime(); // 获取开始时间
        //判断这一百万个数中是否包含29999这个数
        if (bloomFilter.mightContain(29999)) {
            System.out.println("命中了");
        }
        long endTime = System.nanoTime();   // 获取结束时间
        System.out.println("程序运行时间： " + (endTime - startTime) + "纳秒");
    }

    /**
     * 测试误判
     */
    @Test
    public void testFault(){
        for (int i = 0; i < size; i++) {
            bloomFilter.put(i);
        }
        List<Integer> faults = new ArrayList<>(1000);
        // 看 1000 个数字中会有多少误判
        for (int i = 0; i < 1000; i++) {
            int testNum = i + 1 + size;
            boolean mightContain = bloomFilter.mightContain(testNum);
            if(mightContain){
                faults.add(testNum);
            }
        }
        System.out.println("误判断数量为"+faults.size());
        System.out.println("误判的数为:"+faults);
    }
}

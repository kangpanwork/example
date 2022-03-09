package com.sanri.test.algorithm.leetcode;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 动态规划类算法
 */
public class DynamicPlanning {

    /**
     * 爬楼梯问题 : 有 N 阶楼梯 , 每次可以上一阶或者两阶,求有多少种上楼梯的方法
     * 定义函数 f(x) 为有 x 阶楼梯的上楼梯方法数,那么 f(x) = f(x - 1) + f(x - 2)
     * @param n
     * @return
     */
    public int climbStairs(int n){
        if (n <= 2){
            return n ;
        }
        int f1 = 1 , f2 = 2 ;
        for (int i = 3; i <= n; i++) {
            int cur = f1 + f2;
            f1 = f2;
            f2 = cur;
        }
        return f2;
    }

    /**
     * 斐波那契数列 , 好像也不需要递归
     * f(x) = f(x - 1) + f(x - 2)
     * @param n
     * @return
     */
    public int fibonacci(int n){
        if (n <= 2){
            return 1;
        }
        int f1 = 1 , f2 = 1;
        for (int i = 3; i <= n ; i++) {
            int cur = f1 + f2 ;
            f1 = f2;
            f2 = cur;
        }
        return f2;
    }

    /**
     * 抢劫一排住户，但是不能抢邻近的住户，求最大抢劫量
     * 定义最大抢劫量为 f(x) x 为当数组长度为 x 的最大抢劫量,则要么抢第 x 家的 , 要么不抢 x 家的
     *  f(x) = Math.max(f(x - 1),f(x - 2) + nums[x])
     * @param nums
     * @return
     */
    public int rob(int[] nums){
        if (nums.length == 0){
            return 0 ;
        }
        if (nums.length == 1){
            return nums[0];
        }
        int [] dp = new int[nums.length + 1];       // dp[0] 表示没有住户的时候抢劫量为 0 ,dp[1] 表示只有一个住户的时候抢劫量为 nums[0]
        dp[0] = 0 ;
        dp[1] = nums[0];
        for (int i = 2; i <= nums.length; i++) {
            dp[i] = Math.max(dp[i-1],dp[i - 2] + nums[i - 1]);
        }
        return dp[nums.length];
    }

    @Test
    public void testRobChild(){
        int[] arr = {10,12,13};
        System.out.println(rob(arr));
    }

    @Test
    public void testRob(){
        for (int i = 1; i < 10; i++) {
            int[] ints = genArr(i);
            System.out.println(StringUtils.join(ints,','));
            System.out.println(rob(ints));

            System.out.println("-------------------");
        }
    }

    @Test
    public void testClimbStairs(){
        for (int i = 1; i < 10; i++) {
            System.out.print(climbStairs( i)+" ");
        }

    }

    @Test
    public void testFibonacci(){
        for (int i = 1; i < 10; i++) {
            System.out.print(fibonacci( i)+" ");
        }
    }

    private int [] genArr(int n ){
        int [] arr = new int[n];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = RandomUtils.nextInt(10,20);
        }
        return arr;
    }

    @Test
    public void testLongsubString(){
        System.out.println(findLongestSubstringLength("arabcsadfsadfasacfr"));
    }

    private static int findLongestSubstringLength(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        int maxLength = 0;
        int curLength = 0;
        int[] positions = new int[26];
        // 字符位置初始化为-1，负数表示没出现过
        Arrays.fill(positions, -1);

        for (int i = 0; i < str.length(); i++) {
            int curChar = str.charAt(i) - 'a';
            int prePosition = positions[curChar];
            //当前字符与它上次出现位置之间的距离
            int distance = i - prePosition;
            //当前字符第一次出现，或者前一个非重复子字符串中没有包含当前字符
            if (prePosition < 0 || distance > curLength) {
                curLength++;
            } else {
                //更新最长非重复子字符串的长度
                if (curLength > maxLength) {
                    maxLength = curLength;
                }
                curLength = distance;
            }
            //更新字符出现的位置
            positions[curChar] = i;
        }
        if (curLength > maxLength) {
            maxLength = curLength;
        }
        return maxLength;
    }

    @Test
    public void test(){
        int [] arr = new int[10];
        Object var = arr;
        int length = Array.getLength(var);
        System.out.println(length);
    }
}

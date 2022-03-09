package com.sanri.test.testspring;

import org.springframework.core.Constants;

import java.util.HashMap;
import java.util.Map;

public class ConstantUtil {

    public static final int MAX_NUM = 5;
    public static final int MIN_NUM = 2;
    public static final String NAME = "fsx";
    public static final Map<String,Object> MAP = new HashMap<>();

    public static void main(String[] args) {
        Constants constants = new Constants(ConstantUtil.class);
        System.out.println(constants.asNumber("MAX_NUM").intValue()); // 5
        System.out.println(constants.asString("NAME")); //fsx

        // 自动总个数
        System.out.println(constants.getSize()); //3

        //=============它的好处是提供一些批量获取的方法，在属性很多的时候  这个特别好用==============

        // 匹配前缀 批量获取 注意：此处返回的是字段名，不是值  不区分大小写  下同
        System.out.println(constants.getNames("M")); //[MIN_NUM, MAX_NUM, MAP]
        System.out.println(constants.getNames("m")); //[MIN_NUM, MAX_NUM, MAP]
        // 后缀匹配
        System.out.println(constants.getNamesForSuffix("NUM")); //[MIN_NUM, MAX_NUM]

        // 拿到所有的值  前缀匹配
        System.out.println(constants.getValues("M")); //[{}, 2, 5]
        // 后缀匹配
        System.out.println(constants.getValuesForSuffix("E")); //[fsx]
    }
}

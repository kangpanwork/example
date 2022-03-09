package com.sanri.test.apachecommons;

import com.alibaba.fastjson.JSON;
import com.sanri.test.reflect.RandomDataService;
import com.sanri.test.reflect.beans.People;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.WordUtils;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * apache commons text
 */
public class Text {

    /**
     * 字符转义功能
     */
    @Test
    public void escape(){
        System.out.println("escape,把特殊符号编码");
        System.out.println(StringEscapeUtils.escapeHtml3("<html>"));
        System.out.println(StringEscapeUtils.escapeHtml4("<html>"));
        System.out.println(StringEscapeUtils.escapeJava("String><;"));
        RandomDataService randomDataService = new RandomDataService();
        Object populateData = randomDataService.populateData(People.class);
        System.out.println(StringEscapeUtils.escapeJson(JSON.toJSONString(populateData)));
    }

    @Test
    public void word(){
        System.out.println("单词处理类");
        System.out.println(WordUtils.capitalize("hello world. i am sanri")); //所有单词的首字母都大写了,与 StringUtil 不一样
        System.out.println(WordUtils.swapCase("aBcDEF"));
    }

    /**
     * 文本相似度计算
     */
    @Test
    public void similarity() {
        JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
        String str1 = "网站拒绝重复写信功能，";
        String str2 = "网站拒绝重复写信功能";
        double value = jaccardSimilarity.apply(str1, str2);
        System.out.println("相似度=" + value + "\n");
    }

    /**
     * 随机字符串构建
     */
    @Test
    public void generateRandomString(){
        RandomStringGenerator.Builder builder = new RandomStringGenerator.Builder();
        RandomStringGenerator generator = null;
        String s = "";

        //使用字母 a-z
        generator = builder.withinRange('a', 'z').build();
        s = generator.generate(20);
        System.out.println(StringUtils.center("随机字母字符串", 20, "="));
        System.out.println(s);

        //使用数字 0-9
        generator = builder.withinRange('0', '9').build();
        s = generator.generate(20);
        System.out.println(StringUtils.center("随机数字字符串", 20, "="));
        System.out.println(s);

        //使用字符 0-z
        generator = builder.withinRange('0', 'z').build();
        s = generator.generate(20);
        System.out.println(StringUtils.center("随机混合字符串", 20, "="));
        System.out.println(s + "\n");
    }

    /**
     * 占位符替换
     */
    @Test
    public void strSubstitutor(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("user", "admin");
        params.put("password", "123456");
        params.put("system-version", "windows 10");
        params.put("version", null);

        //StrSubstitutor不是线程安全的类
        StringSubstitutor strSubstitutor = new StringSubstitutor(params, "${", "}");
        //是否在变量名称中进行替换
        strSubstitutor.setEnableSubstitutionInVariables(true);
        String s = strSubstitutor.replace("你的用户名是${user},密码是${password}。系统版本${system-${version}}");
        System.out.println(s);
    }
}

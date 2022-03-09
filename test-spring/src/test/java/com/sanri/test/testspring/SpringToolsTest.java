package com.sanri.test.testspring;

import lombok.Cleanup;
import org.junit.Test;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.context.properties.bind.PlaceholdersResolver;
import org.springframework.core.Constants;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.SpringProperties;
import org.springframework.core.SpringVersion;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.*;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SpringToolsTest {

    @Test
    public void testUuidSimple(){
        JdkIdGenerator jdkIdGenerator = new JdkIdGenerator();
        AlternativeJdkIdGenerator alternativeJdkIdGenerator = new AlternativeJdkIdGenerator();
        SimpleIdGenerator simpleIdGenerator = new SimpleIdGenerator();

        System.out.println(jdkIdGenerator.generateId());
        System.out.println(alternativeJdkIdGenerator.generateId());

        System.out.println(simpleIdGenerator.generateId());
        System.out.println(simpleIdGenerator.generateId());
    }

    @Test
    public void testUuid(){
        JdkIdGenerator jdkIdGenerator = new JdkIdGenerator();
        AlternativeJdkIdGenerator alternativeJdkIdGenerator = new AlternativeJdkIdGenerator();
        SimpleIdGenerator simpleIdGenerator = new SimpleIdGenerator();

        Instant start;
        Instant end;
        int count = 1000000;

        //jdkIdGenerator
        start = Instant.now();
        for (int i = 0; i < count; i++) {
            jdkIdGenerator.generateId();
        }
        end = Instant.now();
        System.out.println("jdkIdGenerator循环" + count + "次耗时：" + Duration.between(start, end).toMillis() + "ms");

        //alternativeJdkIdGenerator
        start = Instant.now();
        for (int i = 0; i < count; i++) {
            alternativeJdkIdGenerator.generateId();
        }
        end = Instant.now();
        System.out.println("alternativeJdkIdGenerator循环" + count + "次耗时：" + Duration.between(start, end).toMillis() + "ms");

        //simpleIdGenerator
        start = Instant.now();
        for (int i = 0; i < count; i++) {
            simpleIdGenerator.generateId();
        }
        end = Instant.now();
        System.out.println("simpleIdGenerator循环" + count + "次耗时：" + Duration.between(start, end).toMillis() + "ms");
    }

    @Test
    public void testAssert(){
        boolean  a = true;
        assert  a == false;
    }

    @Test
    public void testSprintAssert(){
        Assert.hasText("","不能为空");
    }

    /**
     * 路径匹配 PathMatch
     */
    @Test
    public void testPathMatch(){
        PathMatcher pathMatcher = new AntPathMatcher();
        //这是我们的请求路径  需要被匹配（理解成匹配controller吧 就很容易理解了）
        String requestPath = "/user/list.htm?username=aaa&departmentid=2&pageNumber=1&pageSize=20";//请求路径
        //路径匹配模版
        String patternPath = "/user/list.htm**";
        assert pathMatcher.match(patternPath, requestPath);
    }

    @Test
    public void testPathMatch2() {
        PathMatcher pathMatcher = new AntPathMatcher();

        // 精确匹配
        assertTrue(pathMatcher.match("/test", "/test"));
        assertFalse(pathMatcher.match("test", "/test"));

        //测试通配符?
        assertTrue(pathMatcher.match("t?st", "test"));
        assertTrue(pathMatcher.match("te??", "test"));
        assertFalse(pathMatcher.match("tes?", "tes"));
        assertFalse(pathMatcher.match("tes?", "testt"));

        //测试通配符*
        assertTrue(pathMatcher.match("*", "test"));
        assertTrue(pathMatcher.match("test*", "test"));
        assertTrue(pathMatcher.match("test/*", "test/Test"));
        assertTrue(pathMatcher.match("*.*", "test."));
        assertTrue(pathMatcher.match("*.*", "test.test.test"));
        assertFalse(pathMatcher.match("test*", "test/")); //注意这里是false 因为路径不能用*匹配
        assertFalse(pathMatcher.match("test*", "test/t")); //这同理
        assertFalse(pathMatcher.match("test*aaa", "testblaaab")); //这个是false 因为最后一个b无法匹配了 前面都是能匹配成功的

        //测试通配符** 匹配多级URL
        assertTrue(pathMatcher.match("/*/**", "/testing/testing"));
        assertTrue(pathMatcher.match("/**/*", "/testing/testing"));
        assertTrue(pathMatcher.match("/bla/**/bla", "/bla/testing/testing/bla/bla")); //这里也是true哦
        assertFalse(pathMatcher.match("/bla*bla/test", "/blaXXXbl/test"));

        assertFalse(pathMatcher.match("/????", "/bala/bla"));
        assertFalse(pathMatcher.match("/**/*bla", "/bla/bla/bla/bbb"));

        assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing/"));
        assertTrue(pathMatcher.match("/*bla*/**/bla/*", "/XXXblaXXXX/testing/testing/bla/testing"));
        assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing"));
        assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing.jpg"));
        assertTrue(pathMatcher.match("/foo/bar/**", "/foo/bar"));

        //这个需要特别注意：{}里面的相当于Spring MVC里接受一个参数一样，所以任何东西都会匹配的
        assertTrue(pathMatcher.match("/{bla}.*", "/testing.html"));
        assertFalse(pathMatcher.match("/{bla}.htm", "/testing.html")); //这样就是false了

        assertTrue(pathMatcher.matchStart("/x/x/**/bla", "/x/x/x/"));
        assertTrue(pathMatcher.matchStart("/?/**","/x/x/x"));       //开始部分是和模式匹配，模式必须完整匹配到字符串
    }

    @Test
    public void testDigest() throws IOException {
        InputStream inputStream = new FileInputStream("d:/test/bigfile");
        DigestUtils.md5DigestAsHex(inputStream);
    }

    @Test
    public void testMini(){
        System.out.println(SpringVersion.getVersion());
        System.out.println(SpringBootVersion.getVersion());
    }

    @Test
    public void testMapUtil(){
        // 大小写不敏感 map
        Map<String, Object> map = new LinkedCaseInsensitiveMap<>();

        map.put("a", 1);
        map.put("A", 1);
        System.out.println(map); //{A=1}
        System.out.println(map.get("a")); //1 map里面key是小写的a，通过大写的A也能get出来结果
        assertTrue(map.get("a") == map.get("A"));

        //多值 map
        //用Map接的时候  请注意第二个泛型 是个List哦
        //Map<String, List<Integer>> map = new LinkedMultiValueMap<>();
        LinkedMultiValueMap<String, Integer> multiValueMap = new LinkedMultiValueMap<>();

        //此处务必注意，如果你还是用put方法  那是没有效果的 同一个key还是会覆盖
        //multiValueMap.put("a", Arrays.asList(1));
        //multiValueMap.put("a", Arrays.asList(1));
        //multiValueMap.put("a", Arrays.asList(1));
        //System.out.println(multiValueMap); //{a=[1]}

        //请用add方法
        multiValueMap.add("a", 1);
        multiValueMap.add("a", 2);
        multiValueMap.add("a", 3);
        System.out.println(multiValueMap); //{a=[1, 1, 1]}
    }

    @Test
    public void test5P(){
        PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("${","}");

    }

    @Test
    public void testPlaceHolder(){
        PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("{","}");
        Properties properties = new Properties();
        properties.setProperty("name","sanri");
        properties.setProperty("sex","1");
        // what 还是会原样输出
        String placeholder = "{what}是否是{name}{sex}";
        String messageForamt = propertyPlaceholderHelper.replacePlaceholders(placeholder, properties);
        System.out.println(messageForamt);

        String replacePlaceholders = propertyPlaceholderHelper.replacePlaceholders(placeholder, new PropertyPlaceholderHelper.PlaceholderResolver() {
            @Override
            public String resolvePlaceholder(String placeholderName) {
                String property = properties.getProperty(placeholderName);
                if(!StringUtils.hasText(property)){
                    return "";
                }
                return property;
            }
        });
        System.out.println(replacePlaceholders);

//        SpringProperties.getProperty("");
    }

    @Test
    public void resource() throws IOException {
        File file = ResourceUtils.getFile("classpath:application.properties");
        System.out.println(file);

        assertTrue(ResourceUtils.isUrl("classpath:application.properties"));

        System.out.println(ResourceUtils.getURL("classpath:application.properties"));

        ClassPathResource classPathResource = new ClassPathResource("application.properties");
        // 每次都会打开一个新的流
        InputStream inputStream = classPathResource.getInputStream();
        inputStream.close();
    }

    /**
     * 端口扫描
     */
    @Test
    public void testSocket(){
        System.out.println(SocketUtils.PORT_RANGE_MAX); //65535 最大端口号
        System.out.println(SocketUtils.findAvailableTcpPort()); //45569 随便找一个可用的Tcp端口 每次执行值都不一样哦
        System.out.println(SocketUtils.findAvailableTcpPort(1000, 2000)); //1325 从指定范围内随便找一个端口

        //找一堆端口出来  并且是排好序的
        System.out.println(SocketUtils.findAvailableTcpPorts(10, 1000, 2000)); //[1007, 1034, 1287, 1483, 1494, 1553, 1577, 1740, 1963, 1981]

        //UDP端口的找寻 同上
        System.out.println(SocketUtils.findAvailableUdpPort()); //12007
    }

    @Test
    public void testClassUtil(){
        System.out.println(ClassUtils.determineCommonAncestor(Integer.class,Long.class)); //class java.lang.Number  找到他们共同父类
        System.out.println(ClassUtils.determineCommonAncestor(Integer.class,Number.class)); //class java.lang.Number  若其中一个就是父类 就直接返回即可
        System.out.println(ClassUtils.determineCommonAncestor(Integer.class, SpringToolsTest.class)); //null  八竿子打不着就返回null吧（注意相当于是Object.class 直接返回null的）
    }

    @Test
    public void testConstant(){
        // 必须大写变量名
        Constants constants = new Constants(SpringConstant.class);
        int num = constants.asNumber("NUM").intValue();
        System.out.println(num);
    }

    @Test
    public void testParamType(){
        Method spelTests = ClassUtils.getMethod(TestBean.class, "spelTests");
        Class<?> returnType = spelTests.getReturnType();
        System.out.println(returnType);
        Class<?> aClass = GenericTypeResolver.resolveReturnTypeArgument(spelTests, returnType);
        System.out.println(aClass);
    }

    @Test
    public void testJdkParamType(){
        Method spelTests = ClassUtils.getMethod(TestBean.class, "spelTests");
        Type genericReturnType = spelTests.getGenericReturnType();
        ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        System.out.println(actualTypeArgument);
        System.out.println(parameterizedType.getOwnerType());
        System.out.println(parameterizedType.getRawType());

//        Class<?> returnType = spelTests.getReturnType();
//        Type type = returnType.getGenericInterfaces()[0];
//        ParameterizedType genericSuperclass = (ParameterizedType) returnType.getGenericInterfaces()[0];
//        System.out.println(genericSuperclass.getOwnerType());
//        System.out.println(genericSuperclass.getRawType());
//        String typeName = genericSuperclass.getTypeName();
//        Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
//        System.out.println(actualTypeArguments[0].getTypeName());
    }

    @Test
    public void testClass(){
        List a = new ArrayList();
        System.out.println(a.getClass() == ArrayList.class);
        System.out.println(a.getClass() == List.class);
        System.out.println(a instanceof  List);
        System.out.println(TypeUtils.isAssignable(a.getClass(),List.class));
    }
}

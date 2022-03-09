package com.sanri.test.json;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.sanri.test.json.dataprovide.DataProvide;
import com.sanri.test.json.dto.*;
import com.sanri.test.json.settings.DeviceDeSerializer;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 都说 jackson 好用,这里来试一下 jackson 的使用
 *
 * 测试包括 swagger json 的解析
 * 实体 beans 的序列化,反序列化
 * 解析复杂的 json 是否方便等方面
 */
public class JacksonJsonMain {

    DataProvide dataProvide = new DataProvide();

    @Test
    public void testFirstJackson() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        Car car = dataProvide.car();
        String carJson = JSON.toJSONString(car);
        JsonParser jp = jsonFactory.createJsonParser(carJson); // or URL, Stream, Reader, String, byte[]
        if (jp.nextToken() != JsonToken.START_OBJECT) {
            throw new IOException("不可识别的 json ");
        }
        while(jp.nextToken() != JsonToken.END_OBJECT){
            String fieldName = jp.getCurrentName();
            System.out.println(fieldName);
            jp.nextToken();
        }
        jp.close();
    }

    @Test
    public void testNodeTree() throws IOException {
        Car car = dataProvide.car();
        String carJson = JSON.toJSONString(car);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(carJson);
        JsonParser jsonParser = node.traverse();

    }

    @Test
    public void testWriteJson() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        FileOutputStream fileOutputStream = new FileOutputStream("d:/test/sanri.json");
        JsonGenerator jg = jsonFactory.createJsonGenerator(fileOutputStream, JsonEncoding.UTF8); // or Stream, Reader
        jg.writeStartObject();
        jg.writeBooleanField("a",false);
//        jg.writeObjectField("car",dataProvide.car());     // 这里不可以直接写对象
        jg.writeEndObject();
        jg.close();
    }

    /**
     * 最简单的写入为字符串,这个日期会以时间戳[毫秒]写入
     * @throws JsonProcessingException
     */
    @Test
    public void testJackson() throws IOException {
        ComplexDto complexDto = dataProvide.complexDto();
        System.out.println(complexDto);

        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(complexDto);
        System.out.println(s);

        ComplexDto readDto = objectMapper.readValue(s, ComplexDto.class);
        System.out.println(readDto == complexDto);
    }

    /**
     * 测试一些配置
     *
     * 空值
     * 时间格式
     *
     */
    @Test
    public void testJacksonConfig() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Car car = dataProvide.car();
        String carJson = objectMapper.writeValueAsString(car);

        // 反序列化时,会因为缺少字段而异常,加入配置解决
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        CarIgnorePrice carIgnorePrice = objectMapper.readValue(carJson, CarIgnorePrice.class);
        System.out.println(carIgnorePrice);

        // 空值的序列化忽略空属性,这个放到类上去设置了 @JsonInclude(JsonInclude.Include.NON_EMPTY)
        // 设置 @JsonIgnore 不输出到 json
        car.setPrice(null);
        String ignoreNullValueProJson = objectMapper.writeValueAsString(car);
        System.out.println(ignoreNullValueProJson);

        // 日期格式化
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ignoreNullValueProJson = objectMapper.writeValueAsString(car);
        System.out.println(ignoreNullValueProJson);

    }

    /**
     * 测试含有接口的序列化和反序列化
     * 我在用 fastjson 反序列化 swagger 的时候遇到了问题,一些属性会丢失,因为里面用的是接口,fastjson 是用代理类实现的
     */
    @Test
    public void testInterfaceSDS() throws IOException {
        Parking parking = dataProvide.parking();
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(parking);
        System.out.println(s);

        Parking readValue = objectMapper.readValue(s, Parking.class);
        System.out.println(readValue);

    }

//    /**
//     * 自定义反序列化来反序列化对象
//     * 因为 jackson 对于接口是不是支持的
//     * 有问题,不能对内部元素进行单独反序列化吗?
//     */
//    @Test
//    public void testInterfaceSDSRegisterDeserializer() throws IOException {
//        Parking parking = dataProvide.parking();
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        SimpleModule module = new SimpleModule();
//        module.addDeserializer(Device.class, new DeviceDeSerializer());
//        objectMapper.registerModule(module);
//
//        String s = objectMapper.writeValueAsString(parking);
//        System.out.println(s);
//
//        Parking readValue = objectMapper.readValue(s, Parking.class);
//        System.out.println(readValue);
//
//    }

    /**
     * java 对象编写成 yaml 形式
     */
    @Test
    public void writeYamlFile() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        Parking parking = dataProvide.parking();
        String s = objectMapper.writeValueAsString(parking);
        System.out.println(s);
    }

    /**
     * 反序列化时忽略不存在的字段 ，
     * 如果字段不存在， 反序列化会失败
     */
    @Test
    public void testJacksonNullDeserializer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        NullObjectDeserializer nullObjectDeserializer = new NullObjectDeserializer();
        nullObjectDeserializer.setTitle("看看是啥情况");
        String s = objectMapper.writeValueAsString(nullObjectDeserializer);
        System.out.println(s);

        NullObjectDeserializer nullObjectDeserializer1 = objectMapper.readValue("{\"title\":\"看看是啥情况\",\"camera\":{},\"extAttributes\":{}}", NullObjectDeserializer.class);
        System.out.println(nullObjectDeserializer1);
    }

    @Test
    public void testNewLineJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        NullObjectDeserializer nullObjectDeserializer1 = objectMapper.readValue("{\"title\":\r\n\"看看是啥情况\",\r\n\"camera\":{}}", NullObjectDeserializer.class);
        System.out.println(nullObjectDeserializer1);
    }

    @Test
    public void testNewLineJsonOther() throws IOException {
        String text = "{\"traceId\":\"5003223228429107209012\",\"spanId\":\"0\",\"businessId\":null,\"sourceSysId\n" +
                "\":null,\"targetSysId\":null,\"extAttributes\":{},\"deviceID\":\"1017201870F8E79010CE\",\"parentID\":\"\",\"isOnline\":true,\"time\":\"2020-08-12 15:02:17.216\",\"deviceType\":\"2018\"}";
        // fastjson 在属性引号前有 \n 是支持的 jackson 不支持要求更严格
        text = "{\"traceId\":\"5003223228429107209012\",\"spanId\":\"0\",\"businessId\":null,\"sourceSysId\n\":null,\"targetSysId\":null,\"extAttributes\":{},\"deviceID\":\"1017201870F8E79010CE\",\"parentID\":\"\",\"isOnline\":true,\"time\":\"2020-08-12 15:02:17.216\",\"deviceType\":\"2018\"}";
        text = "{\"traceId\":\"5003223228429107209012\",\"spanId\":\"0\",\"businessId\":null,\"sourceSysId\\n\":null,\"targetSysId\":null,\"extAttributes\":{},\"deviceID\":\"1017201870F8E79010CE\",\"parentID\":\"\",\"isOnline\":true,\"time\":\"2020-08-12 15:02:17.216\",\"deviceType\":\"2018\"}";
        DeviceStatusCommon deviceStatusCommon1 = JSON.parseObject(text, DeviceStatusCommon.class);
        System.out.println(deviceStatusCommon1);

        ObjectMapper objectMapper = new ObjectMapper();
        DeviceStatusCommon deviceStatusCommon = objectMapper.readValue(text, DeviceStatusCommon.class);
        System.out.println(deviceStatusCommon);
    }
}

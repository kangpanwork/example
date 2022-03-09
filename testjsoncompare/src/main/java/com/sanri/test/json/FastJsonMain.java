package com.sanri.test.json;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanri.test.json.dataprovide.DataProvide;
import com.sanri.test.json.dto.Camera;
import com.sanri.test.json.dto.Parking;
import com.sanri.test.json.dto.Ref;
import org.junit.Test;

import java.io.IOException;

/**
 * fastjson 相关测试
 */
public class FastJsonMain {

    DataProvide dataProvide = new DataProvide();

    /**
     * 能反序列化接口,但是有数据丢失
     */
    @Test
    public void testFastJsonInterfaceSDS(){
        Parking parking = dataProvide.parking();
        ObjectMapper objectMapper = new ObjectMapper();
        String s = JSON.toJSONString(parking);
        Parking parseObject = JSON.parseObject(s, Parking.class);
        System.out.println(parseObject);
    }

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void test() throws IOException {
        Ref ref = (Ref) dataProvide.populateData(Ref.class);
        Camera camera = (Camera) dataProvide.populateData(Camera.class);
        ref.setOption(camera);

        String reciveData = JSON.toJSONString(ref);

        Ref reverse = JSON.parseObject(reciveData, Ref.class);
        System.out.println(reverse);

        Ref jackson = objectMapper.readValue(reciveData, Ref.class);
        System.out.println(jackson);
    }
}

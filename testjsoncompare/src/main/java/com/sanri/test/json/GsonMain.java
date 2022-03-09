package com.sanri.test.json;

import com.google.gson.Gson;
import com.sanri.test.json.dataprovide.DataProvide;
import com.sanri.test.json.dto.Parking;
import org.junit.Test;

/**
 * gson 的相关测试
 */
public class GsonMain {

    DataProvide dataProvide = new DataProvide();

    @Test
    public void testS(){
        Gson gson = new Gson();
        String s = gson.toJson(dataProvide.car());
        System.out.println(s);
    }

    @Test
    public void testSDS(){
        Gson gson = new Gson();
        String s = gson.toJson((dataProvide).parking());
        System.out.println(s);
        Parking parking = gson.fromJson(s, Parking.class);
        System.out.println(parking);
    }
}

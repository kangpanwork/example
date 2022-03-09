package datag;

import com.alibaba.fastjson.JSON;
import com.sanri.test.jetcache.utills.RandomDataService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DataGMain {
    RandomDataService randomDataService = new RandomDataService();
    @Test
    public void testC(){
        List<Object>  list = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            Object object = randomDataService.populateData(circuitDto.class);
            list.add(object);
        }
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void testB(){
        List<Object>  list = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            Object object = randomDataService.populateData(LineDto.class);
            list.add(object);
        }
        System.out.println(JSON.toJSONString(list));
    }
}

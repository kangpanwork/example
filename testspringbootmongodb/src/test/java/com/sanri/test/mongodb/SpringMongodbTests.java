package com.sanri.test.mongodb;

import com.mongodb.client.result.DeleteResult;
import com.sanri.test.mongodb.dtos.Goods;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ExecutableFindOperation;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SpringMongodbTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    RandomDataService randomDataService = new RandomDataService();

    @Test
    public void testInsert(){
        Object goods = randomDataService.populateData(Goods.class);
        mongoTemplate.save(goods);
    }

    @Test
    public void testInsertBatch(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 100; i++) {
            Object goods = randomDataService.populateData(Goods.class);
            mongoTemplate.save(goods);
        }
        stopWatch.stop();
        System.out.println("插入 100 条数据用时:"+stopWatch.getTime()+" ms");
    }

    @Test
    public void testFindData(){
        List<Goods> all = mongoTemplate.findAll(Goods.class);
        for (Goods goods : all) {
            System.out.println(goods);
        }
    }

    @Test
    public void testFindById(){
        Goods byId = mongoTemplate.findById(900, Goods.class);
        System.out.println(byId);

    }

    @Test
    public void testFindCustom(){
        Query query = new Query(Criteria.where("name").is("昌姬"));
        List<Goods> goods = mongoTemplate.find(query, Goods.class);
        System.out.println("总共数量 :"+goods.size());
        for (Goods good : goods) {
            System.out.println(good);
        }

        query = new Query(Criteria.where("price").lt(400f).gt(100f));
        goods = mongoTemplate.find(query, Goods.class);
        System.out.println("价格在 100 到  400 的有 "+ goods.size() +" 条");
        for (Goods good : goods) {
            System.out.println(good);
        }
    }

    @Test
    public void testRemove(){
        Query query = new Query(Criteria.where("name").regex("雪"));
//        List<Goods> goods = mongoTemplate.find(query, Goods.class);
//        for (Goods good : goods) {
//            System.out.println(good);
//        }

        DeleteResult remove = mongoTemplate.remove(query, Goods.class);
        System.out.println(remove.getDeletedCount());
    }

    @Test
    public void countQuery(){
        Query query = new Query(Criteria.where("name").regex("丁"));
        long count = mongoTemplate.count(query, Goods.class);
        System.out.println(count);
    }

}

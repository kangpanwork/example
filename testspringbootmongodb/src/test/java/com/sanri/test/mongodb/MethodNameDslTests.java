package com.sanri.test.mongodb;

import com.sanri.test.mongodb.dtos.Goods;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MethodNameDslTests {
    @Autowired
    GoodsRepository goodsRepository;

    @Test
    public void findByName(){
        List<Goods> query = goodsRepository.findByName("雪");
        System.out.println(query);
    }

    @Test
    public void findById(){
        Goods goods = goodsRepository.findById(911);
        System.out.println(goods);
    }

    @Test
    public void between(){
        List<Goods> byPriceBetween = goodsRepository.findByPriceBetween(100f, 400f);
        for (Goods goods : byPriceBetween) {
            System.out.println(goods);
        }
    }

    @Test
    public void findLike(){
        List<Goods> goods = goodsRepository.queryAllByNameIsLike("雪");
        System.out.println(goods);
    }
}

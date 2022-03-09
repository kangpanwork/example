package com.sanri.test.mongodb;

import com.sanri.test.mongodb.dtos.Goods;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 使用 dsl 查询语言, 先要运行插件,生成查询对象
 */
@Repository
public interface GoodsRepository extends MongoRepository<Goods,String> , QuerydslPredicateExecutor<Goods> {
    List<Goods> findByName(String name);

    List<Goods> queryAllByNameIsLike(String name);

    Goods findById(Integer id);

    List<Goods> findByPriceBetween(Float min,Float max);

    long countByBrand(String brand);
}

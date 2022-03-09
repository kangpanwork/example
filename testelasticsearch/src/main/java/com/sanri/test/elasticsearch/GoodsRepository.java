package com.sanri.test.elasticsearch;

import com.sanri.test.elasticsearch.es.entity.GoodsItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.List;
 
/**
 * @author langpf 2019/2/27
 */
@Component
public interface GoodsRepository extends ElasticsearchRepository<GoodsItem, Long> {
    /**
     * @Description:根据价格区间查询
     * @Param price1
     * @Param price2
     */
    List<GoodsItem> findByPriceBetween(double price1, double price2);
}
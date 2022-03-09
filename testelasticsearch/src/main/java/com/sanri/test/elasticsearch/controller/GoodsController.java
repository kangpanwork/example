package com.sanri.test.elasticsearch.controller;

import com.sanri.test.elasticsearch.GoodsRepository;
import com.sanri.test.elasticsearch.RandomDataService;
import com.sanri.test.elasticsearch.ZTRoomInquiryRepository;
import com.sanri.test.elasticsearch.es.entity.GoodsItem;
import com.sanri.test.elasticsearch.es.entity.ZTSubscribeRoomInquiryDto;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class GoodsController {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ZTRoomInquiryRepository ztRoomInquiryRepository;

    RandomDataService randomDataService = new RandomDataService();

    @GetMapping("/createIndex")
    public void createIndex(){
        elasticsearchTemplate.createIndex(GoodsItem.class);
        elasticsearchTemplate.putMapping(GoodsItem.class);
    }

    @GetMapping("/save")
    public void save(){
        GoodsItem goodsItem = new GoodsItem(1L, "小米手机7", " 手机",
                "小米", 3499.00, "http://image.baidu.com/13123.jpg");
        goodsRepository.save(goodsItem);
    }

    @GetMapping("/savssss")
    public void savssss(){
        ZTSubscribeRoomInquiryDto ztSubscribeRoomInquiryDto = (ZTSubscribeRoomInquiryDto) randomDataService.populateData(ZTSubscribeRoomInquiryDto.class);
        ztRoomInquiryRepository.save(ztSubscribeRoomInquiryDto);
    }

    @GetMapping("/delete")
    public void delete(Long id){
        goodsRepository.deleteById(id);
    }

    @GetMapping("/update")
    public void update(Long id,String title){
        Optional<GoodsItem> byId = goodsRepository.findById(id);
        GoodsItem goodsItem = byId.get();
        goodsItem.setTitle(title);
        goodsRepository.save(goodsItem);
    }

    @GetMapping("/findAll")
    public Iterator<GoodsItem> findAll(){
        Iterable<GoodsItem> iterable = goodsRepository.findAll(Sort.by("price").ascending());
        Iterator<GoodsItem> iterator = iterable.iterator();
       return iterator;
    }

    @GetMapping("/saveAll")
    public int saveAll(){
        List<GoodsItem> list = new ArrayList<>();
        list.add(new GoodsItem(1L, "小米手机7", "手机", "小米", 3299.00, "http://image.baidu.com/13123.jpg"));
        list.add(new GoodsItem(2L, "坚果手机R1", "手机", "锤子", 3699.00, "http://image.baidu.com/13123.jpg"));
        list.add(new GoodsItem(3L, "华为META10", "手机", "华为", 4499.00, "http://image.baidu.com/13123.jpg"));
        list.add(new GoodsItem(4L, "小米Mix2S", "手机", "小米", 4299.00, "http://image.baidu.com/13123.jpg"));
        list.add(new GoodsItem(5L, "荣耀V10", "手机", "华为", 2799.00, "http://image.baidu.com/13123.jpg"));

        // 接收对象集合，实现批量新增
        goodsRepository.saveAll(list);
        return list.size();
    }

    @GetMapping("/matchQuery")
    public Page<GoodsItem> matchQuery(){
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("title", "小米手机"))
//                .withQuery(QueryBuilders.termQuery("price", 998.0))         // termQuery
//                .withQuery(QueryBuilders.fuzzyQuery("title", "faceoooo"))   // fuzzyQuery
                .build();
        Page<GoodsItem> search = goodsRepository.search(nativeSearchQuery);
        return search;
    }

    @GetMapping("/pageQuery")
    public Page<GoodsItem> pageQuery(){
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.termQuery("category", "手机"))
                .withPageable(PageRequest.of(0, 2))
//                .withSort(SortBuilders.fieldSort("price").order(SortOrder.ASC))           //排序

                .build();

        Page<GoodsItem> search = goodsRepository.search(nativeSearchQuery);
        return search;
    }


    // （1）统计某个字段的数量
//     ValueCountBuilder vcb=  AggregationBuilders.count("count_uid").field("uid");
// （2）去重统计某个字段的数量（有少量误差）
//     CardinalityBuilder cb= AggregationBuilders.cardinality("distinct_count_uid").field("uid");
// （3）聚合过滤
//     FilterAggregationBuilder fab= AggregationBuilders.filter("uid_filter")
//                                   .filter(QueryBuilders.queryStringQuery("uid:001"));
// （4）按某个字段分组
//     TermsBuilder tb=  AggregationBuilders.terms("group_name").field("name");
// （5）求和
//     SumBuilder  sumBuilder=	AggregationBuilders.sum("sum_price").field("price");
// （6）求平均
//     AvgBuilder ab= AggregationBuilders.avg("avg_price").field("price");
// （7）求最大值
//     MaxBuilder mb= AggregationBuilders.max("max_price").field("price");
// （8）求最小值
//     MinBuilder min=	AggregationBuilders.min("min_price").field("price");
// （9）按日期间隔分组
//     DateHistogramBuilder dhb= AggregationBuilders.dateHistogram("dh").field("date");
// （10）获取聚合里面的结果
//     TopHitsBuilder thb=  AggregationBuilders.topHits("top_result");
// （11）嵌套的聚合
//     NestedBuilder nb= AggregationBuilders.nested("negsted_path").path("quests");
// （12）反转嵌套
//    AggregationBuilders.reverseNested("res_negsted").path("kps ");

    @GetMapping("/aggBucketSearch")
    public void aggBucketSearch(){
        NativeSearchQuery nativeSearch = new NativeSearchQueryBuilder()
                .withSourceFilter(new FetchSourceFilter(new String[]{""}, null))
                // 1、添加一个新的聚合, 聚合类型为terms, 聚合名称为brands, 聚合字段为brand ;按照 brands 进行分组统计
                .addAggregation(AggregationBuilders.terms("brand").field("brand"))
//                .addAggregation()
                .build();

        AggregatedPage<GoodsItem> search = (AggregatedPage<GoodsItem>) goodsRepository.search(nativeSearch);
        // 根据字段类型强转 Brand是String，term聚合，所以结果要强转为StringTerm类型
        StringTerms brands = (StringTerms)search.getAggregation("brand");

        List<StringTerms.Bucket> buckets = brands.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            // 3.4、获取桶中的key, 即品牌名称
            System.out.println(bucket.getKeyAsString());
            // 3.5、获取桶中的文档数量
            System.out.println(bucket.getDocCount());
        }
    }

    @GetMapping("/aggSubSearch")
    public void aggSubSearch(){
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withSourceFilter(new FetchSourceFilter(new String[]{""}, null))
                .addAggregation(AggregationBuilders.terms("brand").field("brand")
                        // 在品牌聚合桶内进行嵌套聚合, 求平均值
                        .subAggregation(AggregationBuilders.avg("priceAvg").field("price")))
                .build();

        AggregatedPage aggregatedPage = (AggregatedPage<GoodsItem>)goodsRepository.search(nativeSearchQuery);
        Aggregation aggregation = aggregatedPage.getAggregation("brand");

        StringTerms brands = (StringTerms) aggregation;
        List<StringTerms.Bucket> buckets = brands.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            // 3.4、获取桶中的key, 即品牌名称  3.5、获取桶中的文档数量
            System.out.println(bucket.getKeyAsString() + ", 共" + bucket.getDocCount() + "台");

            // 3.6.获取子聚合结果：
            InternalAvg avg = (InternalAvg) bucket.getAggregations().asMap().get("priceAvg");
            System.out.println("平均售价：" + avg.getValue());
        }
    }
}

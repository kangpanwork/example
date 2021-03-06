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
        GoodsItem goodsItem = new GoodsItem(1L, "????????????7", " ??????",
                "??????", 3499.00, "http://image.baidu.com/13123.jpg");
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
        list.add(new GoodsItem(1L, "????????????7", "??????", "??????", 3299.00, "http://image.baidu.com/13123.jpg"));
        list.add(new GoodsItem(2L, "????????????R1", "??????", "??????", 3699.00, "http://image.baidu.com/13123.jpg"));
        list.add(new GoodsItem(3L, "??????META10", "??????", "??????", 4499.00, "http://image.baidu.com/13123.jpg"));
        list.add(new GoodsItem(4L, "??????Mix2S", "??????", "??????", 4299.00, "http://image.baidu.com/13123.jpg"));
        list.add(new GoodsItem(5L, "??????V10", "??????", "??????", 2799.00, "http://image.baidu.com/13123.jpg"));

        // ???????????????????????????????????????
        goodsRepository.saveAll(list);
        return list.size();
    }

    @GetMapping("/matchQuery")
    public Page<GoodsItem> matchQuery(){
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("title", "????????????"))
//                .withQuery(QueryBuilders.termQuery("price", 998.0))         // termQuery
//                .withQuery(QueryBuilders.fuzzyQuery("title", "faceoooo"))   // fuzzyQuery
                .build();
        Page<GoodsItem> search = goodsRepository.search(nativeSearchQuery);
        return search;
    }

    @GetMapping("/pageQuery")
    public Page<GoodsItem> pageQuery(){
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.termQuery("category", "??????"))
                .withPageable(PageRequest.of(0, 2))
//                .withSort(SortBuilders.fieldSort("price").order(SortOrder.ASC))           //??????

                .build();

        Page<GoodsItem> search = goodsRepository.search(nativeSearchQuery);
        return search;
    }


    // ???1??????????????????????????????
//     ValueCountBuilder vcb=  AggregationBuilders.count("count_uid").field("uid");
// ???2?????????????????????????????????????????????????????????
//     CardinalityBuilder cb= AggregationBuilders.cardinality("distinct_count_uid").field("uid");
// ???3???????????????
//     FilterAggregationBuilder fab= AggregationBuilders.filter("uid_filter")
//                                   .filter(QueryBuilders.queryStringQuery("uid:001"));
// ???4????????????????????????
//     TermsBuilder tb=  AggregationBuilders.terms("group_name").field("name");
// ???5?????????
//     SumBuilder  sumBuilder=	AggregationBuilders.sum("sum_price").field("price");
// ???6????????????
//     AvgBuilder ab= AggregationBuilders.avg("avg_price").field("price");
// ???7???????????????
//     MaxBuilder mb= AggregationBuilders.max("max_price").field("price");
// ???8???????????????
//     MinBuilder min=	AggregationBuilders.min("min_price").field("price");
// ???9????????????????????????
//     DateHistogramBuilder dhb= AggregationBuilders.dateHistogram("dh").field("date");
// ???10??????????????????????????????
//     TopHitsBuilder thb=  AggregationBuilders.topHits("top_result");
// ???11??????????????????
//     NestedBuilder nb= AggregationBuilders.nested("negsted_path").path("quests");
// ???12???????????????
//    AggregationBuilders.reverseNested("res_negsted").path("kps ");

    @GetMapping("/aggBucketSearch")
    public void aggBucketSearch(){
        NativeSearchQuery nativeSearch = new NativeSearchQueryBuilder()
                .withSourceFilter(new FetchSourceFilter(new String[]{""}, null))
                // 1???????????????????????????, ???????????????terms, ???????????????brands, ???????????????brand ;?????? brands ??????????????????
                .addAggregation(AggregationBuilders.terms("brand").field("brand"))
//                .addAggregation()
                .build();

        AggregatedPage<GoodsItem> search = (AggregatedPage<GoodsItem>) goodsRepository.search(nativeSearch);
        // ???????????????????????? Brand???String???term?????????????????????????????????StringTerm??????
        StringTerms brands = (StringTerms)search.getAggregation("brand");

        List<StringTerms.Bucket> buckets = brands.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            // 3.4??????????????????key, ???????????????
            System.out.println(bucket.getKeyAsString());
            // 3.5??????????????????????????????
            System.out.println(bucket.getDocCount());
        }
    }

    @GetMapping("/aggSubSearch")
    public void aggSubSearch(){
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withSourceFilter(new FetchSourceFilter(new String[]{""}, null))
                .addAggregation(AggregationBuilders.terms("brand").field("brand")
                        // ???????????????????????????????????????, ????????????
                        .subAggregation(AggregationBuilders.avg("priceAvg").field("price")))
                .build();

        AggregatedPage aggregatedPage = (AggregatedPage<GoodsItem>)goodsRepository.search(nativeSearchQuery);
        Aggregation aggregation = aggregatedPage.getAggregation("brand");

        StringTerms brands = (StringTerms) aggregation;
        List<StringTerms.Bucket> buckets = brands.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            // 3.4??????????????????key, ???????????????  3.5??????????????????????????????
            System.out.println(bucket.getKeyAsString() + ", ???" + bucket.getDocCount() + "???");

            // 3.6.????????????????????????
            InternalAvg avg = (InternalAvg) bucket.getAggregations().asMap().get("priceAvg");
            System.out.println("???????????????" + avg.getValue());
        }
    }
}

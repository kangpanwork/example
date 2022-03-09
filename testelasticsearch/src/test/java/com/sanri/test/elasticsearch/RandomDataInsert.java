package com.sanri.test.elasticsearch;

import com.sanri.test.elasticsearch.es.entity.GeoLocation;
import com.sanri.test.elasticsearch.es.entity.ZTSubscribeRoomInquiryDto;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import java.awt.print.Pageable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 给地址位置搜索随机数据
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RandomDataInsert {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ZTRoomInquiryRepository ztRoomInquiryRepository;

    private static final String PERSON_INDEX_NAME = "elastic_search_project";
    private static final String PERSON_INDEX_TYPE = "geoLocation";

    RandomDataService randomDataService = new RandomDataService();

    @Test
    public void testInsert(){
        ZTSubscribeRoomInquiryDto ztSubscribeRoomInquiryDto = (ZTSubscribeRoomInquiryDto) randomDataService.populateData(ZTSubscribeRoomInquiryDto.class);
        ztRoomInquiryRepository.save(ztSubscribeRoomInquiryDto);
    }

    /**
     * 在 90 万数据中,查找附近 100m 的人
     */
    @Test
    public void testQuery(){
        double lat = 39.929986;
        double lon = 116.395645;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        GeoDistanceQueryBuilder builder = QueryBuilders.geoDistanceQuery("location").point(lat,lon)
                .distance(100, DistanceUnit.METERS);

        GeoDistanceSortBuilder sortBuilder  = SortBuilders.geoDistanceSort("location", lat, lon)
                .unit(DistanceUnit.METERS).order(SortOrder.ASC);

        PageRequest pageable = PageRequest.of(0,50);

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withFilter(builder).withSort(sortBuilder).withPageable(pageable);
        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();

        List<GeoLocation> geoLocations = elasticsearchTemplate.queryForList(nativeSearchQuery, GeoLocation.class);
        System.out.println(geoLocations);

        stopWatch.stop();
        System.out.println("耗时: "+stopWatch.getTotalTimeMillis() +" ms");
    }

    /**
     * 随机 100 万数据
     */
    @Test
    public void testRandom(){
        double lat = 39.929986;
        double lon = 116.395645;

        // 创建索引
        if (!elasticsearchTemplate.indexExists(PERSON_INDEX_NAME)){
            elasticsearchTemplate.createIndex(PERSON_INDEX_NAME);
        }
        int couter = 0 ;

        List<IndexQuery> queries = new ArrayList<>();
        for (int i = 100000; i < 1000000; i++) {
            double max = 0.00001;
            double min = 0.000001;
            Random random = new Random();
            double s = random.nextDouble() % (max - min + 1) + max;
            DecimalFormat df = new DecimalFormat("######0.000000");
            // System.out.println(s);
            String lons = df.format(s + lon);
            String lats = df.format(s + lat);
            Double dlon = Double.valueOf(lons);
            Double dlat = Double.valueOf(lats);

            GeoLocation geoLocation = new GeoLocation((long)i,dlat + "," + dlon);

            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setId(geoLocation.getId() + "");
            indexQuery.setObject(geoLocation);
            indexQuery.setIndexName(PERSON_INDEX_NAME);
            indexQuery.setType(PERSON_INDEX_TYPE);
            queries.add(indexQuery);

            if (queries.size() % 500 == 0){
                elasticsearchTemplate.bulkIndex(queries);
                queries.clear();
                System.out.println("添加了 500 个地理位置 ");
                couter+=500;
            }
        }

        if (queries.size() > 0){
            elasticsearchTemplate.bulkIndex(queries);
            System.out.println("最后添加了 "+queries.size() + " 个位置" );
            couter+= queries.size();
        }

        System.out.println("总共添加地址位置 "+ couter+ " 个");
    }
}

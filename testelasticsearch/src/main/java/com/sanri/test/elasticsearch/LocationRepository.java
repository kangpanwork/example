package com.sanri.test.elasticsearch;

import com.sanri.test.elasticsearch.es.entity.GeoLocation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface LocationRepository extends ElasticsearchRepository<GeoLocation,Long> {

}

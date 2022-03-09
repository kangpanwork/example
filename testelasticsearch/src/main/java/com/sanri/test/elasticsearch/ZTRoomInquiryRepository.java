package com.sanri.test.elasticsearch;

import com.sanri.test.elasticsearch.es.entity.ZTSubscribeRoomInquiryDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ZTRoomInquiryRepository extends ElasticsearchRepository<ZTSubscribeRoomInquiryDto,Long> {
}

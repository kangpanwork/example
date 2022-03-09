package com.sanri.test.elasticsearch.es.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

@Document(indexName = "elastic_search_project",type = "geoLocation",shards = 1,replicas = 0)
@Data
public class GeoLocation {
    @Id
    private Long id;

    @GeoPointField
    private String location;

    public GeoLocation() {
    }

    public GeoLocation(Long id, String location) {
        this.id = id;
        this.location = location;
    }
}

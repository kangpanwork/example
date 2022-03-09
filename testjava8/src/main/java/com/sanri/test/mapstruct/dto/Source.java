package com.sanri.test.mapstruct.dto;

import lombok.Data;

@Data
public class Source {
    private String id;
    private Integer num;
    private Integer totalCount;

    private SubSource subSource;

}
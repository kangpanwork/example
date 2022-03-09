package com.sanri.test.mapstruct.dto;

import lombok.Data;

@Data
public class Target {
    private String id;
    private Integer num;
    private Integer count;

    private SubTarget subTarget;

}
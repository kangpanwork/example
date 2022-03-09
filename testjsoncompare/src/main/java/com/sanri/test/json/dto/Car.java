package com.sanri.test.json.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Car {
    private String color;
    private BigDecimal price;

    @JsonIgnore
    private String createUser;
//    @JsonIgnore
    private Date createTime;
}

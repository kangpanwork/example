package com.sanri.test.json.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class ComplexDto {
    private String name;
    private int age;
    private Date dateTime;
    private BigDecimal assets;
//    private Integer [] favorite;
//    private Double [] doubles;
    private List<String> devices;
    private Set<Car> cars;
}

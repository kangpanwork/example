package com.sanri.test.testmvc.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emp {
    private String name;
    private int age;
    private Date date;


}

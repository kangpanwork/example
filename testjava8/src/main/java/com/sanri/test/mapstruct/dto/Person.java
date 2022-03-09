package com.sanri.test.mapstruct.dto;

import lombok.Data;

@Data
public class Person {
    private String firstName;
    private String lastName;
    private int height;
    private String description;
}
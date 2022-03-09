package com.sanri.test.reflect.beans;

import lombok.Data;

import java.util.List;

/**
 * 自循环 Bean
 */
@Data
public class Menu {
    private String id;
    private String name;
    private List<Menu> childs;
}

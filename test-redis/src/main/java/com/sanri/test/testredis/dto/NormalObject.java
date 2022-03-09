package com.sanri.test.testredis.dto;

import java.io.Serializable;
import java.util.Date;

public class NormalObject implements Serializable {
    private String name;
    private Integer age;
    private Date date;

    public NormalObject() {
    }

    public NormalObject(String name, Integer age, Date date) {
        this.name = name;
        this.age = age;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

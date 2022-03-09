package com.sanri.test.reflect.beans;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
public class Staff extends Person {
    // 排名
    private int range;

    // 引用其它 bean  拥有的设备
    private List<Device> devices;

}

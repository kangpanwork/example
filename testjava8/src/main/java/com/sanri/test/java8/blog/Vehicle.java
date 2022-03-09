package com.sanri.test.java8.blog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    //车架号
    private String vin;
    // 车主手机号
    private String phone;
    // 车主姓名
    private String name;
    // 所属车租车公司
    private Integer companyId;
    // 个人评分
    private Double score;
    //安装的设备列表imei,使用逗号分隔
    private String deviceNos;
}

package com.sanri.test.sharding.mapper.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Order {
    private Long id;
    private Long tableId;
    private Long tableModelId;
    private String tableName;
    private Integer vehicleId;
    private Date vehicleStartDate;
    private String vehicleBrand;
    private String vehicleNo;
    private String vehicleType;
    private Date tableDate;
    private Long userId;
    private String userName;
    private Integer checkItemId;
    private Integer checkItemImportant;
    private String checkItemName;
    private Integer itemId;
    private String itemCheckContent;
    private String itemCheckName;
    private Integer itemDeclared;
    private Integer itemOk;
    private String itemOkUrl;
    private String itemProblemDescription;
    private String itemProblemUrl;
}

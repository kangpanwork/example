package com.sanri.test.elasticsearch.es.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ProjectName: hdb-prod-data
 * @Package: com.evergrande.cloud.prod.api.dto.room
 * @ClassName: ZTRoomInquiryDto
 * @Author: EX_08232419
 * @Description:
 * @Date: 2020-11-18 11:59
 * @Version: 1.0
 */

@Data
@Document(indexName = "zt_subscribe_room_inquiry")
@AllArgsConstructor
public class ZTSubscribeRoomInquiryDto implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Id
    private Long id;
    private Integer sortNo;
    private Integer indexNo;
    private BigDecimal costHouseUnitPrice;
    private BigDecimal costPrice;
    @Field(fielddata = true,type = FieldType.Text)
    private String buildingNo;
    @Field(fielddata = true,type = FieldType.Text)
    private String roomNo;
    private String roomName;
    private String productType;
    private String houseType;

    private Long projectId;
    private Integer saleStockStatus;
    private Long onlineSubscribeId;
    private String roomGuid;
    private Long createDate;
    private Long lastUpdateDate;
    private String isDetele;
    private String buildingUnit;
    private Integer upshelfStatus;

    public ZTSubscribeRoomInquiryDto() {
    }
}

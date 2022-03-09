package com.sanri.test.webmg.mapper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 活动基础信息表
 * </p>
 *
 * @author 9420
 * @since 2021-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String code;

    private String type;

    private String name;

    private String title;

    private String cycle;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String doc;

    private Integer rewardMode;

    private Integer rewardSendType;

    private LocalDateTime rewardTimeStart;

    private String rulesLimit;

    private String target;

    private String extData;

    private Integer status;

    private Boolean isDeleted;

    private String createBy;

    private LocalDateTime createDate;

    private String lastUpdatedBy;

    private LocalDateTime lastUpdateDate;

    private String remark;

    private LocalDateTime rewardTimeEnd;


}

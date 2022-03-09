package com.sanri.test.jetcache.web.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class Tag  implements Serializable {
    private Long tagId;
    private String tagName;
}

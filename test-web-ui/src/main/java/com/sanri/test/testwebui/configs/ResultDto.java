package com.sanri.test.testwebui.configs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto<T> {
    private String errorCode;
    private String prompt;
    private T data;
}

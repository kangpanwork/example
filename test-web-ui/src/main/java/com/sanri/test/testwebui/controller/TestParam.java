package com.sanri.test.testwebui.controller;

import com.sanri.web.validation.custom.EnumIntValue;
import com.sanri.web.validation.custom.EnumStringValue;
import com.sanri.web.validation.custom.Password;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class TestParam {
    private String name;
    private Date begin;
    private int status;

    @Digits(message = "必须填数字,6 位数字，2 位小数",integer = 6,fraction = 2)
    private String digist;

    @Password(strength = Password.Strength.STRONG)
    private String password;

    @EnumStringValue({"open","close"})
    private String enumValue;

    @EnumIntValue({1,5,7})
    private int enumIntValue;

    public TestParam() {
        this.begin = new Date();
    }
}

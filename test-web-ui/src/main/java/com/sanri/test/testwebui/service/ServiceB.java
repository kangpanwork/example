package com.sanri.test.testwebui.service;

import org.springframework.stereotype.Component;

@Component
public class ServiceB implements XXService {
    @Override
    public boolean support(BaseParam baseParam) {
        if("gsc".equalsIgnoreCase(baseParam.getCompany())){return true;}

        return false;
    }

    @Override
    public String postInsurance(Insurance insurance) {
        return "gsc";
    }

    @Override
    public String editFirstUser(BaseParam baseParam, String changeTo) {
        return null;
    }
}

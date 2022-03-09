package com.sanri.test.testwebui.service;

import org.springframework.stereotype.Component;

@Component
public class ServiceA implements XXService {
    @Override
    public boolean support(BaseParam baseParam) {
        if("ha".equalsIgnoreCase(baseParam.getCompany())){return true;}

        return false;
    }

    @Override
    public String postInsurance(Insurance insurance) {
        return "ha";
    }

    @Override
    public String editFirstUser(BaseParam baseParam, String changeTo) {
        return null;
    }

}

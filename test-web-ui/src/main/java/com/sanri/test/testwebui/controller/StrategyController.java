package com.sanri.test.testwebui.controller;

import com.sanri.test.testwebui.service.BaseParam;
import com.sanri.test.testwebui.service.Insurance;
import com.sanri.test.testwebui.service.XXService;
import com.sanri.test.testwebui.service.XXServiceAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StrategyController implements XXServiceAware {

    private XXService xxService;

    @GetMapping("/postInsurance")
    public void postInsurance(BaseParam baseParam){
        Insurance insurance = new Insurance();
        insurance.setBaseParam(baseParam);
        xxService.postInsurance(insurance);
    }

    @Override
    public void setXXService(XXService xxService) {
        this.xxService = xxService;
    }
}

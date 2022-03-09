package com.sanri.test.testmvc.proxy;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RpcClient(url = "http://localhost:8080/")
public interface InterfaceTarget {
    @GetMapping("/query")
    String queryByName(String name);

    @PostMapping(value = "/postEmpNormal",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String postEmpNormal(Emp emp);

    @PostMapping(value = "/postEmpJson",consumes = MediaType.APPLICATION_JSON_VALUE)
    String postEmpJson(Emp emp);

    @GetMapping("/findEmp")
    Emp findEmp(String name);

    @PostMapping(value = "/emptyQuery",consumes = MediaType.APPLICATION_JSON_VALUE)
    Emp emptyQuery();

}

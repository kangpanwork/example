package com.sanri.test.jasypt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author 9420
 */
@Service
@Slf4j
public class SomeService {
    @Value("${test.mm}")
    private String testmm;

    @PostConstruct
    public void init(){
      log.info("testmm is {}",testmm);
    }
}

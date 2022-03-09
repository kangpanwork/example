package com.sanri.test.testmvc.dispatchbiz;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

/**
 * 模拟 kafka 监听数据
 */
@Component
@Slf4j
public class KafkaListener {
    @Autowired
    AuditMappingHandlerMapping auditMappingHandlerMapping;

    public void listener(String record){
        AuditEventExtend auditEventExtend = JSON.parseObject(record, AuditEventExtend.class);
        try {
            auditMappingHandlerMapping.handle(auditEventExtend);
        } catch (InvocationTargetException e) {
            log.error("执行目标类出错 {} ",e.getMessage(),e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

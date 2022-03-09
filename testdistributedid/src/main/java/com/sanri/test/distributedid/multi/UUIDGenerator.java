package com.sanri.test.distributedid.multi;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDGenerator implements IdGenerator {
    @Override
    public String generateId(int bizType) {
        return UUID.randomUUID().toString().replace("-","");
    }
}

package com.sanri.test.distributedid.multi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 雪花算法 id 生成
 */
@Component
@Slf4j
public class SnowFlakeGenerator implements IdGenerator{
    UniqueOrderGenerate uniqueOrderGenerate = new UniqueOrderGenerate(1, 0);

    @Override
    public String generateId(int bizType) {
        return uniqueOrderGenerate.nextId()+"";
    }
}

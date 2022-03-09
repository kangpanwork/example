package com.sanri.test.distributedid;

import com.baidu.fsg.uid.UidGenerator;
import com.sanri.test.distributedid.multi.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class IdGeneratorTests {
    @Autowired
    UUIDGenerator uuidGenerator;
    @Autowired
    DatabaseIncrId databaseIncrId;
    @Autowired
    DatabaseParagraph databaseParagraph;
    @Autowired
    RedisIncrId redisIncrId;
    @Autowired
    private SnowFlakeGenerator snowFlakeGenerator;
    @Autowired
    private BaiduUidGenerator baiduUidGenerator;

    @Autowired
    UidGenerator uidGenerator;

    @Before
    public void init(){
        databaseParagraph.init();
    }

    /**
     * 测试所有的生成器是否可用
     */
    @Test
    public void canUse(){
        int bizType = 1000;
        log.info(uuidGenerator.generateId(bizType));
        log.info(databaseIncrId.generateId(bizType));
        log.info(databaseParagraph.generateId(bizType));
        log.info(redisIncrId.generateId(bizType));
        log.info(snowFlakeGenerator.generateId(bizType));
        log.info(baiduUidGenerator.generateId(bizType));
    }

    @Test
    public void redisCanUse(){
        int bizType = 1000;
        log.info(redisIncrId.generateId(bizType));
    }

    @Test
    public void uidGenerator(){
        long uid = uidGenerator.getUID();
        System.out.println(uid);
    }
}

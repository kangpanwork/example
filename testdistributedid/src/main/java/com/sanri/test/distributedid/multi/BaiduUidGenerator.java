package com.sanri.test.distributedid.multi;

import com.baidu.fsg.uid.UidGenerator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaiduUidGenerator implements IdGenerator{
    @Autowired
    UidGenerator uidGenerator;

    @Override
    public String generateId(int bizType) {
        long uid = uidGenerator.getUID();
        return uid+"";
    }
}

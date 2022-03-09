package com.sanri.test.testspringbootkafka.proxy;

import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * 默认消息匹配器,计算发起数据和源数据的 md5 ,都一致时认为匹配
 * 基本所有情况都需要重写这个,这里只是给一个示例
 */
@Component
public class DefaultMessageMatch implements MessageMatch{

    @Override
    public boolean match(byte[] request, byte[] response) {
        if(request != null && response != null) {
            String sourceMd5 = DigestUtils.md5DigestAsHex(request);
            String targetMd5 = DigestUtils.md5DigestAsHex(response);
            return sourceMd5.equals(targetMd5);
        }

        return false;
    }
}

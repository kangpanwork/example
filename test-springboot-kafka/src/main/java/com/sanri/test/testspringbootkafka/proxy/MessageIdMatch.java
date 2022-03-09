package com.sanri.test.testspringbootkafka.proxy;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class MessageIdMatch implements MessageMatch {
    @Override
    public boolean match(byte[] request, byte[] response) {
        try {
            JSONObject requestMessage = JSON.parseObject(new String(request,"utf-8"));
            JSONObject responseMessage = JSON.parseObject(new String(request,"utf-8"));

            return requestMessage.getString("messageId").equals(responseMessage.getString("messageId"));
        } catch (UnsupportedEncodingException e) {
        }
        return false;
    }
}

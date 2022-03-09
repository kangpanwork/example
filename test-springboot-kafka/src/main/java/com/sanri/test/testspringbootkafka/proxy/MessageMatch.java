package com.sanri.test.testspringbootkafka.proxy;

@FunctionalInterface
public interface MessageMatch {
    /**
     * 消息是否匹配目标发起消息
     * @param request
     * @param response
     *
     * @return
     */
    boolean match(byte [] request,byte [] response);
}

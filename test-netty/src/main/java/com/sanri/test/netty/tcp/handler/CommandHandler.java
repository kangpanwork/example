package com.sanri.test.netty.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.support.BeanNameGenerator;

public interface CommandHandler {
    /**
     * 处理数据
     * @param ctx
     * @param payload
     */
    void handler(ChannelHandlerContext ctx,byte [] payload);

    String PREFIX = "COMMAND_HANDLER_";
}

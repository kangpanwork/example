package com.sanri.test.netty.tcp.handler;

import com.sanri.test.netty.protocol.MessageProtocol;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@ChannelHandler.Sharable
public class ServerHandlerDispatch extends ChannelInboundHandlerAdapter {

    @Autowired(required = false)
    private Map<String,CommandHandler> commandHandlerMap = new HashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageProtocol messageProtocol = (MessageProtocol) msg;
        byte command = messageProtocol.getCommand();
        CommandHandler commandHandler = commandHandlerMap.get(CommandHandler.PREFIX + command);
        if (commandHandler == null){
            log.error("当前命令[{}],不支持",command);
            return ;
        }
        commandHandler.handler(ctx,messageProtocol.getPayload());
    }
}

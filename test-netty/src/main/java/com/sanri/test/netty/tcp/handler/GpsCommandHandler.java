package com.sanri.test.netty.tcp.handler;

import com.sanri.test.netty.protocol.Command;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component(value = CommandHandler.PREFIX + Command.gpsOnline)
@Slf4j
public class GpsCommandHandler implements CommandHandler {
    @Override
    public void handler(ChannelHandlerContext ctx, byte[] payload) {

    }
}

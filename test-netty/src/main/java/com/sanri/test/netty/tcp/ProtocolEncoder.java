package com.sanri.test.netty.tcp;

import com.sanri.test.netty.protocol.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtocolEncoder extends MessageToByteEncoder<MessageProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        try {
            out.writeShort(msg.getStart());
            out.writeByte(msg.getLength());
            out.writeBytes(new byte[]{msg.getCommand()});
            if (msg.getPayload() != null && msg.getPayload().length > 0) {
                out.writeBytes(msg.getPayload());
            }
            out.writeShort(msg.getEnd());
        }catch (Exception e){
            log.error("编码异常 , 这个错如果不捕获 , 出错了都不知道是哪里:{}",e.getMessage(),e);
        }
    }
}

package com.sanri.test.netty.tcp;

import com.sanri.test.netty.protocol.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 协议解析
 * 数据包格式
 * +——----——+——-----——+——----——+
 * |协议开始标志|  长度      | 协议号   |   数据    | 结束标志
 * +——----——+——-----——+——----——+
 */
public class ProtocolDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 数据包长度必须大于基本长度 start+length+command 2+1+1
        if (in.readableBytes() < 4){
            return ;
        }
        // 防止 socket 流攻击, 不接受太大的数据
        if (in.readableBytes() > 2048){
            in.skipBytes(in.readableBytes());
            return ;
        }

        // 先标记读的地方, 如果不是开始,则还原
        in.markReaderIndex();

        short readShort = in.readShort();
        if (readShort != MessageProtocol.PROTOCOL_START){
            in.resetReaderIndex();
            return ;
        }

        byte length = in.readByte();
        byte command = in.readByte();
        if (length == 1){
            // 如果只有一个长度 , 则没有 payload, 检测是不是结束
            short end = in.readShort();
            if (end != MessageProtocol.PROTOCOL_END){   // 最后字节不是结束协议,说明读错了
                in.resetReaderIndex();
                return ;
            }
            out.add(MessageProtocol.createMessage(command));
            return ;
        }
        byte payloadLength = (byte) (length - 1);
        // 长度不够, 等待下次数据到来
        if (in.readableBytes() < payloadLength){
            return ;
        }
        byte [] payload = new byte[payloadLength];
        in.readBytes(payload);
        // 再看最后一个数据是不是结束, 如果不是结束,则说明读错了 ; 但这个应该不会出现
        short endMark = in.readShort();
        if (endMark != MessageProtocol.PROTOCOL_END){
            in.resetReaderIndex();
            return ;
        }
        out.add(MessageProtocol.createMessage(command,payload));
    }
}

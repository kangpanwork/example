package com.sanri.test.netty.tcp.handler;

import com.sanri.test.netty.protocol.Command;
import com.sanri.test.netty.protocol.LoginMessage;
import com.sanri.test.netty.protocol.MessageProtocol;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.stereotype.Component;


@Component(value = CommandHandler.PREFIX + Command.login)
@Slf4j
public class LoginCommandHandler implements CommandHandler {
    @Override
    public void handler(ChannelHandlerContext ctx, byte[] payload) {
//        log.info("登录指令 IMEI: [{}]",payload);
        // 数据解析
        LoginMessage loginMessage = LoginMessage.parse(payload);
        log.info("登录指令 IMEI: [{}]",loginMessage);

        // 回复客户端数据
        if ("0123456789012345".equals(loginMessage.getImei())) {
            MessageProtocol reply = MessageProtocol.createMessage((byte) 0x01);
            ctx.writeAndFlush(reply);
        }else{
            MessageProtocol reply = MessageProtocol.createMessage((byte) 0x44);
            ctx.writeAndFlush(reply);
        }
    }
}

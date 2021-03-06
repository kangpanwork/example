package com.sanri.test.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandle extends SimpleChannelInboundHandler<String> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("新客户端连接:"+ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        System.out.println("服务端收到消息:"+msg);

//        channelHandlerContext.channel().writeAndFlush("hi");
//        channelHandlerContext.writeAndFlush("hi");
//        channelHandlerContext.write("A");
//        channelHandlerContext.write("BC");
        channelHandlerContext.write("DEFG");
//        channelHandlerContext.write("HI");
//        channelHandlerContext.write("12345678910");
        channelHandlerContext.flush();
    }
}

package com.sanri.test.netty.tcp;

import com.sanri.test.netty.tcp.handler.ServerHandlerDispatch;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TcpServerStart implements InitializingBean, DisposableBean {
    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    @Autowired
    private ServerHandlerDispatch serverHandlerDispatch;

    private Channel channel;

    @Value("${track.server.port:20001}")
    private int port;

    @Override
    public void afterPropertiesSet() throws Exception {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>(){

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new ProtocolDecoder());
                pipeline.addLast(new ProtocolEncoder());
                pipeline.addLast(serverHandlerDispatch);
            }
        })
                .option(ChannelOption.SO_BACKLOG, 10 * 1024) // 用于临时存放三次握手的请求的队列的最大长度
                .option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 64 * 1024) // 写的缓冲区高水位线（64*1024=65536）
                .childOption(ChannelOption.SO_RCVBUF, 64 * 1024)
                .childOption(ChannelOption.SO_SNDBUF, 64 * 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        channel = b.bind(port).sync().channel();
        log.info("定位器服务端开始监听TCP端口： " + port + " ，等待客户端（设备）连接...");
    }

    @Override
    public void destroy() throws Exception {
        log.info("TCP服务正在停止，销毁TCP服务资源...");
        if (null == channel) {
            log.error("server channel is null");
            bossGroup = null;
            workerGroup = null;
            return;
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
        bossGroup = null;
        workerGroup = null;
        channel = null;
    }
}

package com.sanri.test.netty;

import com.sanri.test.netty.protocol.Command;
import com.sanri.test.netty.protocol.MessageProtocol;
import com.sanri.test.netty.tcp.ProtocolDecoder;
import com.sanri.test.netty.tcp.ProtocolEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

@Slf4j
public class TrackCommandMain {

    @Test
    public void testTrackMain() throws InterruptedException {

    }

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ProtocolEncoder());
                        pipeline.addLast(new ProtocolDecoder());
                        pipeline.addLast(new ClientLoginHandler());
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect("localhost", 20001).sync();
        Channel channel = channelFuture.channel();

        channelFuture.addListener(cf -> {
            if (cf.isSuccess()){
                System.out.println("成功");
            }else{
                System.out.println("失败:");
                channelFuture.cause().printStackTrace();
            }
        });
//        channelFuture.channel().closeFuture().sync();
//
//        worker.shutdownGracefully();
    }

    static class ClientLoginHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            log.info("发送命令 登录");
            byte [] imei = {0x01 ,0x23 ,0x45, 0x67, (byte) 0x89, 0x01, 0x23, 0x45,0x01};
            MessageProtocol message = MessageProtocol.createMessage(Command.login,imei);
            ctx.writeAndFlush(message);
//            byte [] arr = {0x78,0x78,0x0a,0x01,0x01 ,0x23 ,0x45, 0x67, (byte) 0x89, 0x01, 0x23, 0x45,0x01,0x0d,0x0a};
//            ByteBuf firstMesage  = Unpooled.buffer(arr.length);
//            firstMesage.writeBytes(arr);
//            ctx.writeAndFlush(firstMesage);
        }

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("通道注册成功");
        }
    }

    @Test
    public void bcdDecode(){
        final byte code = 0x23;
        String s = bcdToString(new byte[]{code});
        System.out.println(s);
    }

    /**
     * 将String转成BCD码
     *
     * @param s
     * @return
     */
    public static byte [] strToBCDBytes(String s){
        if(s.length () % 2 != 0){
            s = "0" + s;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream ();
        char [] cs = s.toCharArray ();
        for (int i = 0;i < cs.length;i += 2){
            int high = cs [i] - 48;
            int low = cs [i + 1] - 48;
            baos.write (high << 4 | low);
        }
        return baos.toByteArray ();
    }
    /**
     * 将BCD码转成String
     *
     * @param b
     * @return
     */
    public static String bcdToString(byte [] b){
        StringBuffer sb = new StringBuffer ();
        for (int i = 0;i < b.length;i++ ){
            int h = ((b [i] & 0xff) >> 4) + 48;
            sb.append((char) h);
            int l = (b [i] & 0x0f) + 48;
            sb.append ((char) l);
        }
        return sb.toString () ;
    }

    @Test
    public void test(){
        byte [] imei = {0x01 ,0x23 ,0x45, 0x67, (byte) 0x89, 0x01, 0x23, 0x45};
        System.out.println(bcdToString(imei));
    }
}

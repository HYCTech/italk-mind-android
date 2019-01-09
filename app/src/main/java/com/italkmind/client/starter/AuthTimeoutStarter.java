/**  
* Copyright © 2010 - 2018 All Rights Reserved Powered by italkmind
* @Title: AbstractProtocolBodyParse.java
* @version V1.0  
*/
package com.italkmind.client.starter;

import java.net.InetSocketAddress;

import com.italkmind.client.handler.AuthTimeoutHandler;
import com.italkmind.client.handler.ItalkMindMessageDecoder;
import com.italkmind.client.handler.ItalkMindMessageEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @ClassName: AuthTimeoutStarter
 * @Description: 验证超时自动断开
 * @author fern
 * @date 2018年8月26日
 *
 */

public class AuthTimeoutStarter {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 62500;
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new ItalkMindMessageEncoder());
                        pipeline.addLast(new ItalkMindMessageDecoder());
                        pipeline.addLast(new AuthTimeoutHandler());
                    }
                });
        Channel channel;
        try {
            channel = b.connect().sync().channel();
            System.out.println(" started and listen on " + channel.localAddress());
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }
}

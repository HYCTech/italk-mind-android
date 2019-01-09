package com.zhang.netty_lib.netty;


import com.italkmind.client.handler.ItalkMindMessageDecoder;
import com.italkmind.client.handler.ItalkMindMessageEncoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by 张俨 on 2017/10/9.
 */

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    private NettyListener listener;

    public NettyClientInitializer(NettyListener listener) {
        if(listener == null){
            throw new IllegalArgumentException("listener == null ");
        }
        this.listener = listener;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
//        SslContext sslCtx = SSLContext.getDefault()
//                .createSSLEngine(InsecureTrustManagerFactory.INSTANCE).build();

        ChannelPipeline pipeline = ch.pipeline();
//        pipeline.addLast(sslCtx.newHandler(ch.alloc()));    // 开启SSL
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));    // 开启日志，可以设置日志等级
//        pipeline.addLast("IdleStateHandler", new IdleStateHandler(6, 0, 0));
        pipeline.addLast(new ItalkMindMessageEncoder());//解码器 这里要与服务器保持一致
        pipeline.addLast(new ItalkMindMessageDecoder());//编码器 这里要与服务器保持一致
        pipeline.addLast(new NettyClientHandler(listener));
    }
}
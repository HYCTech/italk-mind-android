/**  
* Copyright © 2010 - 2018 All Rights Reserved Powered by italkmind
* @Title: EchoCrcClientStart.java
* @version V1.0  
*/

package com.italkmind.client.starter;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.zip.CRC32;

import com.italkmind.client.handler.ContentCheckMsgHandler;
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
import io.netty.util.CharsetUtil;

/**
 * @ClassName: EchoCrcClientStart
 * @Description: CRC内容校验启动类
 * @author fern
 * @date 2018年8月12日
 *
 */

public class EchoCrcClientStart {
    private static final int REQUEST_TIME = 32;
    private static final int THREAD_NUM = 16;
    final CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);

    private class WorkThread extends Thread {
        private final String host;
        private final int port;
        private final String msgInfo;

        private WorkThread(String host, int port, String msg) {
            this.host = host;
            this.port = port;
            this.msgInfo = msg;
        }

        @Override
        public void run() {
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel channel) throws Exception {
                                ChannelPipeline pipeline = channel.pipeline();
                                pipeline.addLast(new ItalkMindMessageEncoder());
                                pipeline.addLast(new ItalkMindMessageDecoder());
                                pipeline.addLast(new ContentCheckMsgHandler(REQUEST_TIME, msgInfo));
                            }
                        });
                Channel channel = b.connect().sync().channel();
                System.out.println(this.getName() + " started and listen on " + channel.localAddress());
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    group.shutdownGracefully().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String msg = "hello netty!";
        CRC32 crc = new CRC32();
        crc.update(msg.getBytes(CharsetUtil.UTF_8));
        System.out.println("发送字符串的crc值为" + crc.getValue());

        EchoCrcClientStart starter = new EchoCrcClientStart();
        for (int i = 0; i < THREAD_NUM; i++) {
            Thread workThread = starter.new WorkThread("127.0.0.1", 62500, msg);
            workThread.start();

        }
        starter.countDownLatch.await();
        System.out.println("main thread finished!!");
    }
}

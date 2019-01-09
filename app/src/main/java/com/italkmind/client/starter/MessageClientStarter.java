/**  
* Copyright © 2010 - 2018 All Rights Reserved Powered by italkmind
* @Title: AbstractProtocolBodyParse.java
* @version V1.0  
*/
package com.italkmind.client.starter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

import com.italkmind.client.api.LogicApiClient;
import com.italkmind.client.handler.ImClientHandler;
import com.italkmind.client.handler.ItalkMindMessageDecoder;
import com.italkmind.client.handler.ItalkMindMessageEncoder;
import com.italkmind.client.protocol.ProtocolBodyParse;
import com.italkmind.client.protocol.ProtocolBodyParseHelper;
import com.italkmind.client.util.ClientTools;
import com.italkmind.client.vo.api.LoginInfo;
import com.italkmind.client.vo.protocol.TItalkMindMessage;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
* @ClassName: MessageClientStarter
* @Description: 消息交互启动器
* @author fern
* @date 2018年9月5日
*
*/

public class MessageClientStarter {
    private Channel channel = null;
    private Semaphore semaphore = new Semaphore(1);
    private CountDownLatch lathch = new CountDownLatch(1);
    
    private class WorkThread extends Thread {
        private final LoginInfo info;

        private WorkThread(LoginInfo info) {
            this.info = info;//拷贝
        }

        @Override
        public void run() {
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(info.getLinkInfo().getMajorLinkIp(), 
                            info.getLinkInfo().getMajorLinkPort()))
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel channel) throws Exception {
                                ChannelPipeline pipeline = channel.pipeline();
                                pipeline.addLast(new ItalkMindMessageEncoder());
                                pipeline.addLast(new ItalkMindMessageDecoder());
                                pipeline.addLast(new ImClientHandler(info.getLinkInfo().getAuthId(), Long.valueOf(info.getUserInfo().getUid())));
                            }
                        });
                channel = b.connect().sync().channel();
                ClientTools.msgLog(this.getName() + " started and listen on " + channel.localAddress());
                semaphore.release();
                channel.closeFuture().sync();
                ClientTools.msgLog("子线程退出====");
                lathch.countDown();
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
    
    public static void main(String[] args) throws IOException, InterruptedException {
        String account = "18695732196";
        String password = "abc123";
        if (args.length == 2) {
            account = args[0];
            password = args[1];
        }
        
        LoginInfo info = LogicApiClient.login(account, password);
        ClientTools.msgLog("获取的处理结果" + info.toString());
        MessageClientStarter starter = new MessageClientStarter();
        
        WorkThread wt = starter.new WorkThread(info);
        wt.start();
        ClientTools.msgLog("启动线程，等待线程启动完成");
        starter.semaphore.acquire();
        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
        help();
        while (true) {
            try {
                String input = scanner.readLine();
                List<String> parms = Arrays.stream(input.split(",")).map(item->item.trim()).collect(Collectors.toList());
                int cmdType = Integer.parseInt(parms.get(0));
                if ((-1 == cmdType) || !starter.channel.isActive()) {
                    break;
                }

                if (parms.size() != 3) {
                    help();
                    continue;
                }
                
                ProtocolBodyParse parser = ProtocolBodyParseHelper.fetchProtocolBodyParse(cmdType)/*.get()*/;
                parms.remove((int)0);
                TItalkMindMessage<?> message = parser.genMessage(parms);
                ClientTools.msgLog("发送的消息为 - " + message.toString());
                starter.channel.writeAndFlush(message);
                System.out.println();
                System.out.println("======================================================================");
                System.out.println();
            } catch (RuntimeException ex) {
                ClientTools.msgLog("处理过程中存在异常");
                ex.printStackTrace();
            }

        }
        
        if (starter.channel.isActive()) {
            starter.channel.close();
        }
        ClientTools.msgLog("等待子线程退出");
        starter.lathch.await();
        ClientTools.msgLog("退出主线程");
    }
    
    private static void help() {
        ClientTools.msgLog("输入消息类型，-1表示退出。");
        ClientTools.msgLog("具体的消息发送方式为<命令类型>,<接收者类型>,<接收者id>");
        ClientTools.msgLog("例如：10,1,153762082810000");
    }
}

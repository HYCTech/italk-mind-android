/**  
* Copyright © 2010 - 2018 All Rights Reserved Powered by italkmind
* @Title: EchoClientHandler.java
* @version V1.0  
*/

package com.italkmind.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @ClassName: EchoClientHandler
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author fern
 * @date 2018年8月12日
 *
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private final int maxTimes;
    private int counter = 0;

    public EchoClientHandler(int requestTimes) {
        this.maxTimes = requestTimes;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        if (msg.readableBytes() < Long.BYTES) {
            System.out.println("now, counter = " + counter + ", and readable len = " + msg.readableBytes());
            return;
        }
        System.out.println("now, counter = " + counter + ", and readable len = " + msg.readableBytes());
        long crc = msg.readLong();
        System.out.println(Thread.currentThread().getName() + " counter: " + counter + ", Client received: " + crc);
        if (++counter == this.maxTimes) {
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

//    @Override
//    protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
//        if (msg.readableBytes() < Long.BYTES) {
//            System.out.println("now, counter = " + counter + ", and readable len = " + msg.readableBytes());
//            return;
//        }
//        System.out.println("now, counter = " + counter + ", and readable len = " + msg.readableBytes());
//        long crc = msg.readLong();
//        System.out.println(Thread.currentThread().getName() + " counter: " + counter + ", Client received: " + crc);
//        if (++counter == this.maxTimes) {
//            ctx.close();
//        }
//    }
}

/**  
* Copyright © 2010 - 2018 All Rights Reserved Powered by italkmind
* @Title: EchoClientHandler.java
* @version V1.0  
*/

package com.italkmind.client.handler;

import java.nio.charset.Charset;
import java.util.zip.CRC32;

import com.italkmind.client.protocol.ProtocolBodyParseHelper;
import com.italkmind.client.util.ClientContants;
import com.italkmind.client.util.ClientTools;
import com.italkmind.client.vo.protocol.ItalkMindMessage;
import com.italkmind.client.vo.protocol.body.HeaderMessage.ItalkMindHeader;
import com.italkmind.client.vo.protocol.body.SystemMessage;
import com.italkmind.client.vo.protocol.body.SystemMessage.ContentCheckReq;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @ClassName: EchoClientHandler
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author fern
 * @date 2018年8月12日
 *
 */
public class ContentCheckMsgHandler extends SimpleChannelInboundHandler<ItalkMindMessage> {
    private final int maxTimes;
    private final int crc32Value;
    private final String context;
    private int counter = 0;
    

    public ContentCheckMsgHandler(int requestTimes, String context) {
        this.maxTimes = requestTimes;
        this.crc32Value = this.calCar32(context);
        this.context = context;
    }
    
    private int calCar32(String context) {
        CRC32 crc32 = new CRC32();
        crc32.update(context.getBytes(Charset.forName("utf8")));
        return (int)crc32.getValue();
    }
    
    public ItalkMindMessage fetchContentCheckAckMsg() {
        ItalkMindHeader.Builder header = ItalkMindHeader.newBuilder();
        header.setCheckCode(ClientContants.MSG_CHECK_CODE);
        header.setCmdType(ProtocolBodyParseHelper.CONTENT_CHECK_REQ.getCode());
        header.setMessageId(ClientTools.getId());
        
        ContentCheckReq.Builder body = ContentCheckReq.newBuilder();
        body.setContent(this.context);
        return new ItalkMindMessage(header.build(), body.build());
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(this.fetchContentCheckAckMsg());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ItalkMindMessage msg) throws Exception {
        if (msg.getHeader().getCmdType() != ProtocolBodyParseHelper.CONTENT_CHECK_ACK.getCode()) {
            ctx.fireChannelRead(msg);
            return;
        }
        SystemMessage.ContentCheckAck ackBody = (SystemMessage.ContentCheckAck)msg.getBody();
        if (ackBody.getCrc32() == this.crc32Value) {
            System.out.println(Thread.currentThread().getName() + " - crc32 check ok : " + counter);
        }

        if (++counter == this.maxTimes) {
            ctx.close();
        }

        ctx.writeAndFlush(this.fetchContentCheckAckMsg());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
//netty5
//    @Override
//    protected void messageReceived(ChannelHandlerContext ctx, ItalkMindMessage msg) throws Exception {
//        if (msg.getHeader().getCmdType() != ProtocolBodyParseHelper.CONTENT_CHECK_ACK.getCode()) {
//            ctx.fireChannelRead(msg);
//            return;
//        }
//        SystemMessage.ContentCheckAck ackBody = (SystemMessage.ContentCheckAck)msg.getBody();
//        if (ackBody.getCrc32() == this.crc32Value) {
//            System.out.println(Thread.currentThread().getName() + " - crc32 check ok : " + counter);
//        }
//
//        if (++counter == this.maxTimes) {
//            ctx.close();
//        }
//
//        ctx.writeAndFlush(this.fetchContentCheckAckMsg());
//    }
}

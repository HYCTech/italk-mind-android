/**  
* Copyright © 2010 - 2018 All Rights Reserved Powered by italkmind
* @Title: AbstractProtocolBodyParse.java
* @version V1.0  
*/
package com.italkmind.client.handler;

import java.util.concurrent.TimeUnit;

import com.italkmind.client.protocol.ProtocolBodyParseHelper;
import com.italkmind.client.util.ClientTools;
import com.italkmind.client.vo.protocol.ItalkMindMessage;
import com.italkmind.client.vo.protocol.body.SystemMessage.ConnectAuthReq;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @ClassName: ImClientHandler
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author fern
 * @date 2018年9月6日
 *
 */

public class ImClientHandler extends SimpleChannelInboundHandler<ItalkMindMessage> {
    private final long uid;
    private final String authId;
    private boolean authOK = false;

    public ImClientHandler(String authId, long uid) {
        this.authId = authId;
        this.uid = uid;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ItalkMindMessage message = ClientTools.fetchRawMessage(ClientTools.getId(),
                ProtocolBodyParseHelper.CONNECT_AUTH_REQ.getCode());
        ConnectAuthReq.Builder builder = ConnectAuthReq.newBuilder();
        builder.setUserId(this.uid);
        builder.setTokenId(this.authId);
        message.setBody(builder.build());
        ctx.writeAndFlush(message);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ItalkMindMessage msg) throws Exception {
        this.heartBeat(ctx, msg);
        ClientTools.msgLog("接收到的数据为：");
        System.out.println(msg.toString());
    }

    private void heartBeat(ChannelHandlerContext ctx, ItalkMindMessage msg) {
        if (this.authOK) {
            return;
        }
        if (msg.getHeader().getCmdType() == 2) {
            this.authOK = true;
            ctx.executor().scheduleWithFixedDelay(()->{
                ctx.writeAndFlush(ClientTools.fetchRawMessage(ClientTools.getId(),
                        ProtocolBodyParseHelper.HEART_BEAT_REQ.getCode()));
            }, 0, 15, TimeUnit.SECONDS);
        }
    }

//    @Override
//    protected void messageReceived(ChannelHandlerContext ctx, ItalkMindMessage msg) throws Exception {
//        this.heartBeat(ctx, msg);
//        ClientTools.msgLog("接收到的数据为：");
//        System.out.println(msg.toString());
//    }
}

/**  
* Copyright © 2010 - 2018 All Rights Reserved Powered by italkmind
* @Title: AbstractProtocolBodyParse.java
* @version V1.0  
*/
package com.italkmind.client.handler;

import com.italkmind.client.protocol.ProtocolBodyParseHelper;
import com.italkmind.client.util.ClientContants;
import com.italkmind.client.util.ClientTools;
import com.italkmind.client.vo.protocol.ItalkMindMessage;
import com.italkmind.client.vo.protocol.body.HeaderMessage.ItalkMindHeader;
import com.italkmind.client.vo.protocol.body.SystemMessage.ContentCheckReq;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @ClassName: AuthTimeoutHandler
 * @Description: 认证超时的handler类
 * @author fern
 * @date 2018年8月26日
 *
 */

public class AuthTimeoutHandler extends SimpleChannelInboundHandler<ItalkMindMessage> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("成功建立连接，当前时间为" + System.currentTimeMillis());
        ctx.writeAndFlush(this.fetchContentCheckAckMsg());
    }
    public ItalkMindMessage fetchContentCheckAckMsg() {
        ItalkMindHeader.Builder header = ItalkMindHeader.newBuilder();
        header.setCheckCode(ClientContants.MSG_CHECK_CODE);
        header.setCmdType(ProtocolBodyParseHelper.CONTENT_CHECK_REQ.getCode());
        header.setMessageId(ClientTools.getId());
        
        ContentCheckReq.Builder body = ContentCheckReq.newBuilder();
        body.setContent("AuthTimeoutHandler");
        return new ItalkMindMessage(header.build(), body.build());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ItalkMindMessage msg) throws Exception {
        System.out.println("当前时间为:" + System.currentTimeMillis() + ", 接收到的消息为:" + msg.toString());
        ctx.fireChannelRead(msg);
    }

//    @Override
//    protected void messageReceived(ChannelHandlerContext ctx, ItalkMindMessage msg) throws Exception {
//        System.out.println("当前时间为:" + System.currentTimeMillis() + ", 接收到的消息为:" + msg.toString());
//        ctx.fireChannelRead(msg);
//    }
}

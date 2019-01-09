/**  
* Copyright © 2010 - 2018 All Rights Reserved Powered by italkmind
* @Title: ItalkMindMessageEncoder.java
* @version V1.0  
*/

package com.italkmind.client.handler;

import java.util.List;

import com.italkmind.client.protocol.ProtocolBodyParse;
import com.italkmind.client.util.ClientTools;
import com.italkmind.client.vo.protocol.ItalkMindMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * @ClassName: ItalkMindMessageEncoder
 * @Description: 消息协议转换器
 * @author fern
 * @date 2018年8月19日
 *
 */
public class ItalkMindMessageEncoder extends MessageToMessageEncoder<ItalkMindMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ItalkMindMessage msg, List<Object> out) throws Exception {
        ByteBuf headerLen = Unpooled.buffer(2);
        ByteBuf headerBuf = Unpooled.wrappedBuffer(msg.getHeader().toByteArray());
        headerLen.writeShort(headerBuf.readableBytes());
        ProtocolBodyParse parser = ClientTools.fetchProtocolBodyParse(msg.getHeader());

        ByteBuf bodyBuf = Unpooled.wrappedBuffer(parser.encodeBody(msg.getBody()));
        ByteBuf protocolMsg = Unpooled.wrappedBuffer(headerLen, headerBuf, bodyBuf);
        ByteBuf protocolMsgLen = Unpooled.buffer(2);
        protocolMsgLen.writeShort(protocolMsg.readableBytes());
        out.add(Unpooled.wrappedBuffer(protocolMsgLen, protocolMsg));
    }

}

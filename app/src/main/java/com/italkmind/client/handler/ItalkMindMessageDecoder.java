/**  
* Copyright © 2010 - 2018 All Rights Reserved Powered by italkmind
* @Title: ItalkMindMessageDecoder.java
* @version V1.0  
*/

package com.italkmind.client.handler;

import com.italkmind.client.protocol.ProtocolBodyParse;
import com.italkmind.client.util.ClientTools;
import com.italkmind.client.vo.protocol.ItalkMindMessage;
import com.italkmind.client.vo.protocol.body.HeaderMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @ClassName: ItalkMindMessageDecoder
 * @Description: 协议解码器
 * @author fern
 * @date 2018年8月19日
 *
 */

public class ItalkMindMessageDecoder extends LengthFieldBasedFrameDecoder {
    private static final int MAX_PROTOCOL_LENGTH = 3 * 1024;

    /**
     * 创建一个新的实例 ItalkMindMessageDecoder.
     *
     */
    public ItalkMindMessageDecoder() {
        super(MAX_PROTOCOL_LENGTH, 0, 2, 0, 2);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (null == frame) {
            return null;
        }
        
        int headerLen = frame.readUnsignedShort();

        byte[] headerCont = new byte[headerLen];
        frame.readBytes(headerCont);
        HeaderMessage.ItalkMindHeader header = HeaderMessage.ItalkMindHeader.parseFrom(headerCont);
        ProtocolBodyParse parser = ClientTools.fetchProtocolBodyParse(header);

        byte[] bodyCont = new byte[frame.readableBytes()];
        frame.readBytes(bodyCont);
        return new ItalkMindMessage(header, parser.decodeBody(bodyCont));
    }

}

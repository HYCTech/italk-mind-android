/**  
* Copyright © 2010 - 2018 All Rights Reserved Powered by italkmind
* @Title: ProtocolBodyParseHelper.java
* @version V1.0  
*/

package com.italkmind.client.protocol;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.italkmind.client.util.ClientTools;
import com.italkmind.client.vo.protocol.ItalkMindMessage;
import com.italkmind.client.vo.protocol.TItalkMindMessage;
import com.italkmind.client.vo.protocol.body.PushMessage.MsgAckMessage;
import com.italkmind.client.vo.protocol.body.PushMessage.MsgReqMessage;
import com.italkmind.client.vo.protocol.body.SendItalkMessage;
import com.italkmind.client.vo.protocol.body.SendItalkMessage.SendImageMessage;
import com.italkmind.client.vo.protocol.body.SendItalkMessage.SendPositionMessage;
import com.italkmind.client.vo.protocol.body.SendItalkMessage.SendTextMessage;
import com.italkmind.client.vo.protocol.body.SendItalkMessage.SendVideoMessage;
import com.italkmind.client.vo.protocol.body.SendItalkMessage.SendVoiceMessage;
import com.italkmind.client.vo.protocol.body.SystemMessage.ConnectAuthAck;
import com.italkmind.client.vo.protocol.body.SystemMessage.ConnectAuthReq;
import com.italkmind.client.vo.protocol.body.SystemMessage.ContentCheckAck;
import com.italkmind.client.vo.protocol.body.SystemMessage.ContentCheckReq;
import com.italkmind.client.vo.protocol.body.SystemMessage.ErrorResultAck;

/**
 * @ClassName: ProtocolBodyParseHelper
 * @Description: 协议消息体的协助解析类.需要新增协议码,需要定义新的全局变量以及对应的消息体类型和code
 * @author fern
 * @date 2018年8月21日
 *
 */

public class ProtocolBodyParseHelper {
    /**
     * 默认的系统协议包的消息体
     */
    public static final ProtocolBodyParse COMMON_ACK = new EmptyBodyProtocolBodyParse(0);
    public static final ProtocolBodyParse ERROR_RESULT_ACK = new TProtocolBodyParseImpl<ErrorResultAck>(255,
            ErrorResultAck.class);

    public static final ProtocolBodyParse CONNECT_AUTH_REQ = new TProtocolBodyParseImpl<ConnectAuthReq>(1,
            ConnectAuthReq.class);
    public static final ProtocolBodyParse CONNECT_AUTH_ACK = new TProtocolBodyParseImpl<ConnectAuthAck>(2,
            ConnectAuthAck.class);
    public static final ProtocolBodyParse HEART_BEAT_REQ = new EmptyBodyProtocolBodyParse(3);
    public static final ProtocolBodyParse HEART_BEAT_ACK = new EmptyBodyProtocolBodyParse(4);
    public static final ProtocolBodyParse CONTENT_CHECK_REQ = new TProtocolBodyParseImpl<ContentCheckReq>(5,
            ContentCheckReq.class);
    public static final ProtocolBodyParse CONTENT_CHECK_ACK = new TProtocolBodyParseImpl<ContentCheckAck>(6,
            ContentCheckAck.class);
    /**
     * 通讯协议包的消息体
     */
    public static final ProtocolBodyParse TEXT_MESSAGE = new TProtocolBodyParseImpl<SendTextMessage>(10,
            SendTextMessage.class) {
        private AtomicInteger inc = new AtomicInteger(0);
        @Override
        public TItalkMindMessage<?> genMessage(List<String> parms) {
            SendTextMessage.Builder builder = SendTextMessage.newBuilder().setRecType(Integer.parseInt(parms.get(0))).setRecId(Long.parseLong(parms.get(1)))
                    .setTextBody("发往用户的消息:" + inc.incrementAndGet());
            ItalkMindMessage message = ClientTools.fetchRawMessage(ClientTools.getId(), this.getCode());
            message.setBody(builder.build());
            return message;
        }

    };
    public static final ProtocolBodyParse POSITION_MESSAGE = new TProtocolBodyParseImpl<SendPositionMessage>(11,
            SendPositionMessage.class);
    public static final ProtocolBodyParse IMAGE_MESSAGE = new TProtocolBodyParseImpl<SendImageMessage>(12,
            SendImageMessage.class) {
        @Override
        public TItalkMindMessage<?> genMessage(List<String> parms) {
            SendImageMessage.Builder imgBuilder = SendImageMessage.newBuilder().setRecType(Integer.parseInt(parms.get(0))).setRecId(Long.parseLong(parms.get(1)))
                    .setRawRemotePath("http://c.hiphotos.baidu.com/image/pic/item/b58f8c5494eef01f40ef23e9edfe9925bc317d26.jpg")
                    .setThumbRemotePath("http://c.hiphotos.baidu.com/image/pic/item/b58f8c5494eef01f40ef23e9edfe9925bc317d26.jpg")
                    .setWidth(100).setHeight(100);
            ItalkMindMessage message = ClientTools.fetchRawMessage(ClientTools.getId(), this.getCode());
            message.setBody(imgBuilder.build());
            return message;
        }
    };
    public static final ProtocolBodyParse VIDEO_MESSAGE = new TProtocolBodyParseImpl<SendVideoMessage>(13,
            SendVideoMessage.class) {
        @Override
        public TItalkMindMessage<?> genMessage(List<String> parms) {
            SendVideoMessage.Builder videoBuilder = SendVideoMessage.newBuilder().setRecType(Integer.parseInt(parms.get(0))).setRecId(Long.parseLong(parms.get(1)))
                    .setDuration(30).setThumbRemotePath("thumbRemothPath").setRawRemotePath("rawRemothPath").setWidth(200).setHeight(200);
            ItalkMindMessage message = ClientTools.fetchRawMessage(ClientTools.getId(), this.getCode());
            message.setBody(videoBuilder.build());
            return message;
        }
    };
    public static final ProtocolBodyParse VOICE_MESSAGE = new TProtocolBodyParseImpl<SendVoiceMessage>(14,
            SendVoiceMessage.class) {
        @Override
        public TItalkMindMessage<?> genMessage(List<String> parms) {
            SendVoiceMessage.Builder voiceBuilder = SendVoiceMessage.newBuilder().setRecType(Integer.parseInt(parms.get(0))).setRecId(Long.parseLong(parms.get(1)))
                    .setDuration(30).setRemotepath("");
            ItalkMindMessage message = ClientTools.fetchRawMessage(ClientTools.getId(), 14);
            message.setBody(voiceBuilder.build());
            return message;
        }
    };
    public static final ProtocolBodyParse FILE_MESSAGE = new TProtocolBodyParseImpl<SendVoiceMessage>(16,
            SendVoiceMessage.class) {
        @Override
        public TItalkMindMessage<?> genMessage(List<String> parms) {
            SendItalkMessage.SendFileMessage.Builder voiceBuilder = SendItalkMessage.SendFileMessage.newBuilder().setRecType(Integer.parseInt(parms.get(0))).setRecId(Long.parseLong(parms.get(1)))
                    .setRemotepath("");
            ItalkMindMessage message = ClientTools.fetchRawMessage(ClientTools.getId(), 16);
            message.setBody(voiceBuilder.build());
            return message;
        }
    };
    
    public static final ProtocolBodyParse CODE_SEND_MESSAGE_REC = new TProtocolBodyParseImpl<MsgAckMessage>(30,
            MsgAckMessage.class);
    public static final ProtocolBodyParse CODE_SEND_MESSAGE_ACK = new TProtocolBodyParseImpl<MsgAckMessage>(31,
            MsgAckMessage.class);
    
    /**
     * 系统推送消息的消息体
     */
    public static final ProtocolBodyParse CODE_NEW_MESSAGE_REQ = new EmptyBodyProtocolBodyParse(40);
    public static final ProtocolBodyParse CODE_NEW_MESSAGE_ACK = new TProtocolBodyParseImpl<MsgReqMessage>(41,
            MsgReqMessage.class);

    private static final Map<Integer, ProtocolBodyParse> PARSER_MAP;
    private static final byte[] EMPTY_BODY = new byte[0];

    static {
        Field[] fields = ProtocolBodyParseHelper.class.getFields();
        Map<Integer, ProtocolBodyParse> parserMap = new HashMap<>(fields.length);

        for (Field field : fields) {
            if (field.getType().isAssignableFrom(ProtocolBodyParse.class)) {
                ProtocolBodyParse parser;
                try {
                    parser = (ProtocolBodyParse) field.get(null);
                    if (null != parserMap.put(parser.getCode(), parser)) {
                        throw new RuntimeException("存在重复的协议码:" + parser.getCode());
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        PARSER_MAP = Collections.unmodifiableMap(parserMap);
    }

    public static ProtocolBodyParse fetchProtocolBodyParse(int protocolCode) {
        return PARSER_MAP.get(protocolCode);
    }

    private static class EmptyBodyProtocolBodyParse implements ProtocolBodyParse {
        private final int code;

        EmptyBodyProtocolBodyParse(int code) {
            this.code = code;
        }

        @Override
        public int getCode() {
            return this.code;
        }

        @Override
        public Object decodeBody(byte[] body) {
            return null;
        }

        @Override
        public byte[] encodeBody(Object body) {
            return EMPTY_BODY;
        }

        @Override
        public TItalkMindMessage<?> genMessage(List<String> parms) {
            return ClientTools.fetchRawMessage(ClientTools.getId(), this.getCode());
        }
    }
}

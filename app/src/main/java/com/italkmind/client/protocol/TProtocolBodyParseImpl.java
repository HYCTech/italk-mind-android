/**  
* Copyright © 2010 - 2018 All Rights Reserved Powered by italkmind
* @Title: TProtocolBodyParseImpl.java
* @version V1.0  
*/

package com.italkmind.client.protocol;

import java.lang.reflect.Method;
import java.util.List;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.italkmind.client.vo.protocol.TItalkMindMessage;
import com.italkmind.client.vo.protocol.body.SystemMessage;

/**
 * @ClassName: TProtocolBodyParseImpl
 * @Description: 通用的协议解析方法
 * @author fern
 * @date 2018年8月21日
 *
 */

class TProtocolBodyParseImpl<T extends MessageLite> implements ProtocolBodyParse {
    private static final String PARSE_FROM_METHOD_NAME = "parseFrom";
    private final int code;
//    private final MethodAccess parseFromMethod;
//    private final int parseFromIndex;
    private  Method parseFromMethod;
    private Class<T> clazzs;
    TProtocolBodyParseImpl(int code, Class<T> clazz) {
        this.code = code;
//        this.parseFromMethod = MethodAccess.get(clazz);
//        this.parseFromIndex = this.parseFromMethod.getIndex(PARSE_FROM_METHOD_NAME, byte[].class);
        this.parseFromMethod = null;
        try {
            this.clazzs = clazz;
            this.parseFromMethod = clazz.getMethod(PARSE_FROM_METHOD_NAME,byte[].class);
            this.parseFromMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public Object decodeBody(byte[] body) {
        try {
//             return this.parseFromMethod.invoke(null, this.parseFromIndex, body);
//            String name = this.parseFromMethod.getName();
            return this.parseFromMethod.invoke(null,new Object[]{body});
//            return SystemMessage.ConnectAuthAck.parseFrom(body);
        } catch (Exception e) {
            throw new RuntimeException("调用方法解析时有误", e);
        }
    }

    @Override
    public byte[] encodeBody(Object body) {
        @SuppressWarnings("unchecked")
        T req = (T) body;
        return req.toByteArray();
    }

    public interface FunctionWithInvalidException<T, R> {
        R apply(T t) throws InvalidProtocolBufferException;
    }

    @Override
    public TItalkMindMessage<?> genMessage(List<String> parms) {
        throw new RuntimeException("尚未增加处理对象的命令-" + this.getCode());
    }
}

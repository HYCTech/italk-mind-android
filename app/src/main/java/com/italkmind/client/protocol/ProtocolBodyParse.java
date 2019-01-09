/**  
* Copyright © 2010 - 2018 All Rights Reserved Powered by italkmind
* @Title: ProtocolBodyParse.java
* @version V1.0  
*/
    
package com.italkmind.client.protocol;

import java.util.List;

import com.italkmind.client.vo.protocol.TItalkMindMessage;

/**
* @ClassName: ProtocolBodyParse
* @Description: 解析协议流数据信息工具类
* @author fern
* @date 2018年8月21日
*
*/

public interface ProtocolBodyParse {
    /**
     * 
    * @Title: getCode
    * @Description: 返回协议码
    * @param @return    参数
    * @return int    返回类型
    * @throws
     */
    int getCode();
    /**
     * 
    * @Title: decodeBody
    * @Description: 解析数据流为对象
    * @param @param body
    * @param @return
    * @param @throws InvalidProtocolBufferException    参数
    * @return Object    返回类型
    * @throws
     */
    Object decodeBody(byte[] body);
    /**
     * 
    * @Title: encodeBody
    * @Description: 将对象编码为字符流
    * @param @param body
    * @param @return    参数
    * @return byte[]    返回类型
    * @throws
     */
    byte[] encodeBody(Object body);
    
    /**
     * 
    * @Title: genMessage
    * @Description: 返回验证对象
    * @param @return    参数
    * @return TItalkMindMessage<?>    返回类型
    * @throws
     */
    TItalkMindMessage<?> genMessage(List<String> parms); 
}

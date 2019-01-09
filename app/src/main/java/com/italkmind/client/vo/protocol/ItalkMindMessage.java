/**  
* Copyright © 2010 - 2018 All Rights Reserved Powered by italkmind
* @Title: ItalkMindMessage.java
* @version V1.0  
*/

package com.italkmind.client.vo.protocol;

import com.italkmind.client.vo.protocol.body.HeaderMessage.ItalkMindHeader;

/**
 * @ClassName: ItalkMindMessage
 * @Description: 长连接通讯交互协议实体
 * @author fern
 * @date 2018年8月19日
 *
 */

public class ItalkMindMessage extends TItalkMindMessage<Object> {
    public ItalkMindMessage() {
    }

    public ItalkMindMessage(ItalkMindHeader header, Object body) {
        super(header, body);
    }
}

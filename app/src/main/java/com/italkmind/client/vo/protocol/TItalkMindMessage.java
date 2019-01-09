/**  
* Copyright © 2010 - 2018 All Rights Reserved Powered by italkmind
* @Title: AbstractProtocolBodyParse.java
* @version V1.0  
*/
package com.italkmind.client.vo.protocol;

import android.support.annotation.NonNull;
import java.util.Objects;

import com.italkmind.client.protocol.ProtocolBodyParse;
import com.italkmind.client.vo.protocol.body.HeaderMessage.ItalkMindHeader;


/**
* @ClassName: TItalkMindMessage
* @Description: TODO(这里用一句话描述这个类的作用)
* @author fern
* @date 2018年9月7日
*
*/

public class TItalkMindMessage <T> {
    private ItalkMindHeader header;
    private T body;

    public TItalkMindMessage (){

    }

    public TItalkMindMessage(ItalkMindHeader header) {
        this.header = header;
    }

    public TItalkMindMessage(ItalkMindHeader header, T body) {
        this.header = header;
        this.body = body;
    }

    public TItalkMindMessage(T body) {
        this.body = body;
    }

    public boolean equalCmd(@NonNull ProtocolBodyParse parse) {
        return Objects.requireNonNull(header).getCmdType() == parse.getCode();
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public ItalkMindHeader getHeader() {
        return header;
    }

    public void setHeader(ItalkMindHeader header) {
        this.header = header;
    }
}

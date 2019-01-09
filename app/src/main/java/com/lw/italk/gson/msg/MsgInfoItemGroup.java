package com.lw.italk.gson.msg;

import java.util.List;

public class MsgInfoItemGroup {
    private long sendId;
    private int sendType;
    private List<MsgItem> msgInfoItems;

    public long getSendId() {
        return sendId;
    }

    public void setSendId(long sendId) {
        this.sendId = sendId;
    }

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public List<MsgItem> getMsgInfoItems() {
        return msgInfoItems;
    }

    public void setMsgInfoItems(List<MsgItem> msgInfoItems) {
        this.msgInfoItems = msgInfoItems;
    }
}

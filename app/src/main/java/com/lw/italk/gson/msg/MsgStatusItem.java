package com.lw.italk.gson.msg;

/**
 * Created by 喜明 on 2018/8/28.
 */

public class MsgStatusItem {

    private String msgid;
    private int status;

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

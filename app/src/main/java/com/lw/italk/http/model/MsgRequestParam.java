package com.lw.italk.http.model;

/**
 * Created by 喜明 on 2018/8/28.
 */

public class MsgRequestParam {

    private String from_account;
    private String[] msgid;

    public String getFrom_account() {
        return from_account;
    }

    public void setFrom_account(String from_account) {
        this.from_account = from_account;
    }

    public String[] getMsgid() {
        return msgid;
    }

    public void setMsgid(String[] msgid) {
        this.msgid = msgid;
    }
}

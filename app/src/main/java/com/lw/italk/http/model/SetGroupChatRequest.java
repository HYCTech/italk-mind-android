package com.lw.italk.http.model;

/**
 * Created by 喜明 on 2018/9/1.
 */

public class SetGroupChatRequest {
    private String from_account;
    private String groupid;
    private SetGroupChatItem items;

    public String getFrom_account() {
        return from_account;
    }

    public void setFrom_account(String from_account) {
        this.from_account = from_account;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public SetGroupChatItem getItems() {
        return items;
    }

    public void setItems(SetGroupChatItem items) {
        this.items = items;
    }
}

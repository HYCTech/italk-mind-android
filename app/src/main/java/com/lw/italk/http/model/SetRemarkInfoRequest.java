package com.lw.italk.http.model;

/**
 * Created by 喜明 on 2018/8/29.
 */

public class SetRemarkInfoRequest {
    private String from_account;
    private String to_account;
    private RemarkItem items;

    public String getFrom_account() {
        return from_account;
    }

    public void setFrom_account(String from_account) {
        this.from_account = from_account;
    }

    public String getTo_account() {
        return to_account;
    }

    public void setTo_account(String to_account) {
        this.to_account = to_account;
    }

    public RemarkItem getItems() {
        return items;
    }

    public void setItems(RemarkItem items) {
        this.items = items;
    }
}

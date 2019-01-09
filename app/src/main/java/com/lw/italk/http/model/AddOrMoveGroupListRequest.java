package com.lw.italk.http.model;

/**
 * Created by 喜明 on 2018/9/8.
 */

public class AddOrMoveGroupListRequest {
    private String from_account;
    private String[] items;

    public String getFrom_account() {
        return from_account;
    }

    public void setFrom_account(String from_account) {
        this.from_account = from_account;
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }
}

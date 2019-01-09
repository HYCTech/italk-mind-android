package com.lw.italk.http.model;

import org.json.JSONObject;

/**
 * Created by 喜明 on 2018/8/26.
 */

public class ChangeUserInfoRequest {
    private String userid;
    private JSONObject items;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public JSONObject getItems() {
        return items;
    }

    public void setItems(JSONObject items) {
        this.items = items;
    }
}

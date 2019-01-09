package com.lw.italk.gson.friend;

import com.lw.italk.gson.friend.GetAddFrienditem;

import java.util.List;

/**
 * Created by lxm on 2018/8/23.
 */
public class GetAddFriendList {
    private long timestampnow;
    private int totalnum;
    private List<GetAddFrienditem> items;

    public long getTimestampnow() {
        return timestampnow;
    }

    public void setTimestampnow(long timestampnow) {
        this.timestampnow = timestampnow;
    }

    public int getTotalnum() {
        return totalnum;
    }

    public void setTotalnum(int totalnum) {
        this.totalnum = totalnum;
    }

    public List<GetAddFrienditem> getItems() {
        return items;
    }

    public void setItems(List<GetAddFrienditem> items) {
        this.items = items;
    }
}

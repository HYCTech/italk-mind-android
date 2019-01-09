package com.lw.italk.gson.friend;

import com.lw.italk.greendao.model.BlackList;
import com.lw.italk.gson.friend.BlackFriendItem;

import java.util.List;

/**
 * Created by lxm on 2018/8/23.
 */
public class BlackFriendList {
    private long timestampnow;
    private int totalnum;
    private List<BlackList> items;

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

    public List<BlackList> getItems() {
        return items;
    }

    public void setItems(List<BlackList> items) {
        this.items = items;
    }
}

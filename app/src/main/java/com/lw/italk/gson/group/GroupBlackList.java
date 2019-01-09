package com.lw.italk.gson.group;

import java.util.List;

/**
 * Created by lxm on 2018/8/23.
 */
public class GroupBlackList {
    private long timestampnow;
    private int totalnum;
    private List<GroupBlackItem> items;

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

    public List<GroupBlackItem> getItems() {
        return items;
    }

    public void setItems(List<GroupBlackItem> items) {
        this.items = items;
    }
}

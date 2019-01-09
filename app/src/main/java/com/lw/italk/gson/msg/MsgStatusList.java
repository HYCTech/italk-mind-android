package com.lw.italk.gson.msg;

/**
 * Created by 喜明 on 2018/8/28.
 */

public class MsgStatusList {

    private long timestampnow;
    private int totalnum;
    private MsgStatusItem items;

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

    public MsgStatusItem getItems() {
        return items;
    }

    public void setItems(MsgStatusItem items) {
        this.items = items;
    }
}

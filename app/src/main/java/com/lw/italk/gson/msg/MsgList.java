package com.lw.italk.gson.msg;

import java.util.List;

/**
 * Created by 喜明 on 2018/8/27.
 */

public class MsgList {
//    private int totalnum;
//    private long timestampnow;
    private List<MsgInfoItemGroup> msgs;//items

//    public int getTotalnum() {
//        return totalnum;
//    }
//
//    public void setTotalnum(int totalnum) {
//        this.totalnum = totalnum;
//    }
//
//    public long getTimestampnow() {
//        return timestampnow;
//    }
//
//    public void setTimestampnow(long timestampnow) {
//        this.timestampnow = timestampnow;
//    }

    public List<MsgInfoItemGroup> getItems() {
        return msgs;
    }

    public void setItems(List<MsgInfoItemGroup> msgs) {
        this.msgs = msgs;
    }
}

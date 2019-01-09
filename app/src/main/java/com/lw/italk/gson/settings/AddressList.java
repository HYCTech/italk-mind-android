package com.lw.italk.gson.settings;

import java.util.List;

/**
 * Created by lxm on 2018/8/23.
 */
public class AddressList {
    private long timestampnow;
    private int totalnum;
    private List<AddressItem> items;

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

    public List<AddressItem> getItems() {
        return items;
    }

    public void setItems(List<AddressItem> items) {
        this.items = items;
    }
}

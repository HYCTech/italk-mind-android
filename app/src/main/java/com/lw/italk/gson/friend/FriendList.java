package com.lw.italk.gson.friend;

import com.lw.italk.greendao.model.Contact;
import com.lw.italk.gson.friend.FriendItem;

import java.util.List;

/**
 * Created by lxm on 2018/8/23.
 */
public class FriendList {
    private long timestampnow;
    private int totalnum;
    private List<Contact> friends;
    private CompanyInfo companyInfo;
    @Override
    public String toString() {
        return "FriendList{" +
                "timestampnow=" + timestampnow +
                ", totalnum=" + totalnum +
                ", friends=" + friends +
                '}';
    }

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

    public List<Contact> getFriends() {
        return friends;
    }

    public void setFriends(List<Contact> friends) {
        this.friends = friends;
    }

    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }
}

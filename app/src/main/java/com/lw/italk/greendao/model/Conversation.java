package com.lw.italk.greendao.model;


import com.lw.italk.gson.msg.MsgItem;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/8/16 0016.
 */

@Entity
public class Conversation {
    @Id(autoincrement = false)
    private String localid;
    private String userid;
    @Transient
    public List<MsgItem> messages;
    private int unreadMsgCount;
    private String username;
    private boolean isGroup;
    private String members;
    private long timestamp;
    private String imgurl;
    private boolean disturb;
    private boolean msgsettop;
    private boolean showname;
    private boolean readBurn;


    @Generated(hash = 531993054)
    public Conversation(String localid, String userid, int unreadMsgCount, String username, boolean isGroup, String members,
                        long timestamp, String imgurl, boolean disturb, boolean msgsettop, boolean showname, boolean readBurn) {
        this.localid = localid;
        this.userid = userid;
        this.unreadMsgCount = unreadMsgCount;
        this.username = username;
        this.isGroup = isGroup;
        this.members = members;
        this.timestamp = timestamp;
        this.imgurl = imgurl;
        this.disturb = disturb;
        this.msgsettop = msgsettop;
        this.showname = showname;
        this.readBurn = readBurn;
    }

    @Generated(hash = 1893991898)
    public Conversation() {
    }


    public MsgItem getLastMessage() {
        return (this.messages == null || this.messages.size() == 0) ? null : (MsgItem) this.messages.get(this.messages.size() - 1);
    }

    public int getMsgCount() {
        return this.messages == null ? 0 : this.messages.size();
    }

    public List<MsgItem> getMessages() {
        return messages;
    }

    public String getLocalid() {
        return this.localid;
    }

    public void setLocalid(String localid) {
        this.localid = localid;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getUnreadMsgCount() {
        return this.unreadMsgCount;
    }

    public void setUnreadMsgCount(int unreadMsgCount) {
        this.unreadMsgCount = unreadMsgCount;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean getIsGroup() {
        return this.isGroup;
    }

    public void setIsGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

    public String getMembers() {
        return this.members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImgurl() {
        return this.imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public boolean getDisturb() {
        return this.disturb;
    }

    public void setDisturb(boolean disturb) {
        this.disturb = disturb;
    }

    public boolean getMsgsettop() {
        return this.msgsettop;
    }

    public void setMsgsettop(boolean msgsettop) {
        this.msgsettop = msgsettop;
    }

    public boolean getShowname() {
        return this.showname;
    }

    public void setShowname(boolean showname) {
        this.showname = showname;
    }

    public boolean getReadBurn() {
        return this.readBurn;
    }

    public void setReadBurn(boolean readBurn) {
        this.readBurn = readBurn;
    }


}

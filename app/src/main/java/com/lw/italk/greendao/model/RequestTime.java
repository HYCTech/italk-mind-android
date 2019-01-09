package com.lw.italk.greendao.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by wapchief on 2017/7/27.
 * 接收好友请求的类
 */

@Entity
public class RequestTime {
    @Id(autoincrement = false)
    private String userid;
    private long timestampnow;
    private long blacklisttime;
    private long grouplisttime;
    private long msglisttime;
    private long groupmembertime;
    private long lastmsg_sendId;
    private int lastmsg_sendType;

    @Generated(hash = 2101621746)
    public RequestTime(String userid, long timestampnow, long blacklisttime,
            long grouplisttime, long msglisttime, long groupmembertime,
            long lastmsg_sendId, int lastmsg_sendType) {
        this.userid = userid;
        this.timestampnow = timestampnow;
        this.blacklisttime = blacklisttime;
        this.grouplisttime = grouplisttime;
        this.msglisttime = msglisttime;
        this.groupmembertime = groupmembertime;
        this.lastmsg_sendId = lastmsg_sendId;
        this.lastmsg_sendType = lastmsg_sendType;
    }
    @Generated(hash = 2058547085)
    public RequestTime() {
    }
    public String getUserid() {
        return this.userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public long getTimestampnow() {
        return this.timestampnow;
    }
    public void setTimestampnow(long timestampnow) {
        this.timestampnow = timestampnow;
    }
    public long getBlacklisttime() {
        return this.blacklisttime;
    }
    public void setBlacklisttime(long blacklisttime) {
        this.blacklisttime = blacklisttime;
    }
    public long getGrouplisttime() {
        return this.grouplisttime;
    }
    public void setGrouplisttime(long grouplisttime) {
        this.grouplisttime = grouplisttime;
    }
    public long getMsglisttime() {
        return this.msglisttime;
    }
    public void setMsglisttime(long msglisttime) {
        this.msglisttime = msglisttime;
    }
    public long getGroupmembertime() {
        return this.groupmembertime;
    }
    public void setGroupmembertime(long groupmembertime) {
        this.groupmembertime = groupmembertime;
    }
    public long getLastmsg_sendId() {
        return this.lastmsg_sendId;
    }
    public void setLastmsg_sendId(long lastmsg_sendId) {
        this.lastmsg_sendId = lastmsg_sendId;
    }
    public int getLastmsg_sendType() {
        return this.lastmsg_sendType;
    }
    public void setLastmsg_sendType(int lastmsg_sendType) {
        this.lastmsg_sendType = lastmsg_sendType;
    }
    
    
}

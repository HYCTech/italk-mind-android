package com.lw.italk.greendao.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by wapchief on 2017/7/27.
 * 接收好友请求的类
 */

@Entity
public class Message {
    @Id(autoincrement = true)
    private long id;
    private String msgid;
    private long msgtime;
    private long msgdir;
    private long isacked;
    private long isdelivered;
    private long status;
    @NotNull
    private String participant;
    private Long islistened;
    @NotNull
    private String msgbody;
    private String groupname;
    @Generated(hash = 288541790)
    public Message(long id, String msgid, long msgtime, long msgdir, long isacked,
            long isdelivered, long status, @NotNull String participant,
            Long islistened, @NotNull String msgbody, String groupname) {
        this.id = id;
        this.msgid = msgid;
        this.msgtime = msgtime;
        this.msgdir = msgdir;
        this.isacked = isacked;
        this.isdelivered = isdelivered;
        this.status = status;
        this.participant = participant;
        this.islistened = islistened;
        this.msgbody = msgbody;
        this.groupname = groupname;
    }
    @Generated(hash = 637306882)
    public Message() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getMsgid() {
        return this.msgid;
    }
    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }
    public long getMsgtime() {
        return this.msgtime;
    }
    public void setMsgtime(long msgtime) {
        this.msgtime = msgtime;
    }
    public long getMsgdir() {
        return this.msgdir;
    }
    public void setMsgdir(long msgdir) {
        this.msgdir = msgdir;
    }
    public long getIsacked() {
        return this.isacked;
    }
    public void setIsacked(long isacked) {
        this.isacked = isacked;
    }
    public long getIsdelivered() {
        return this.isdelivered;
    }
    public void setIsdelivered(long isdelivered) {
        this.isdelivered = isdelivered;
    }
    public long getStatus() {
        return this.status;
    }
    public void setStatus(long status) {
        this.status = status;
    }
    public String getParticipant() {
        return this.participant;
    }
    public void setParticipant(String participant) {
        this.participant = participant;
    }
    public Long getIslistened() {
        return this.islistened;
    }
    public void setIslistened(Long islistened) {
        this.islistened = islistened;
    }
    public String getMsgbody() {
        return this.msgbody;
    }
    public void setMsgbody(String msgbody) {
        this.msgbody = msgbody;
    }
    public String getGroupname() {
        return this.groupname;
    }
    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }
}

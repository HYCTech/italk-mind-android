package com.lw.italk.greendao.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by wapchief on 2017/7/27.
 * 接收好友请求的类
 */

@Entity
public class Contact {
    @Id(autoincrement = false)
    private String uid;
    private String jid;
    private Long timestampnow;
    private String username;
    private String remark;
    private String avatar;
    private String localimage;
    private String mobile;
    private String email;

    @Generated(hash = 2085314264)
    public Contact(String uid, String jid, Long timestampnow, String username,
            String remark, String avatar, String localimage, String mobile,
            String email) {
        this.uid = uid;
        this.jid = jid;
        this.timestampnow = timestampnow;
        this.username = username;
        this.remark = remark;
        this.avatar = avatar;
        this.localimage = localimage;
        this.mobile = mobile;
        this.email = email;
    }
    @Generated(hash = 672515148)
    public Contact() {
    }
    public String getJid() {
        return this.jid;
    }
    public void setJid(String jid) {
        this.jid = jid;
    }
    public Long getTimestampnow() {
        return this.timestampnow;
    }
    public void setTimestampnow(Long timestampnow) {
        this.timestampnow = timestampnow;
    }
    public String getLocalimage() {
        return this.localimage;
    }
    public void setLocalimage(String localimage) {
        this.localimage = localimage;
    }
    public String getUid() {
        return this.uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

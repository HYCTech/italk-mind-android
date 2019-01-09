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
public class BlackList {
    @Id(autoincrement = false)
    private String userid;
    private String jid;
    private String nickname;
    private String remarkname;
    private String image;
    private long timestamp;
    @Generated(hash = 1381978422)
    public BlackList(String userid, String jid, String nickname, String remarkname,
            String image, long timestamp) {
        this.userid = userid;
        this.jid = jid;
        this.nickname = nickname;
        this.remarkname = remarkname;
        this.image = image;
        this.timestamp = timestamp;
    }
    @Generated(hash = 1200343381)
    public BlackList() {
    }
    public String getUserid() {
        return this.userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getJid() {
        return this.jid;
    }
    public void setJid(String jid) {
        this.jid = jid;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getRemarkname() {
        return this.remarkname;
    }
    public void setRemarkname(String remarkname) {
        this.remarkname = remarkname;
    }
    public String getImage() {
        return this.image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public long getTimestamp() {
        return this.timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

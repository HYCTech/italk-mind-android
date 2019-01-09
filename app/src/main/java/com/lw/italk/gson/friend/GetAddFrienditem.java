package com.lw.italk.gson.friend;

import org.greenrobot.greendao.annotation.Entity;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by lxm on 2018/8/23.
 */
@Entity
public class GetAddFrienditem {
    @Id(autoincrement = true)
    Long id;
    private String userid;
    private String nickname;
    private String remarkInfo;
    private String avatar;
    private int status;//1:已请求（待验证）2:已添加，3:等待接收 4:已拒绝
    private String followAuthId;


    @Generated(hash = 608295337)
    public GetAddFrienditem() {
    }

    @Generated(hash = 1640395932)
    public GetAddFrienditem(Long id, String userid, String nickname,
            String remarkInfo, String avatar, int status, String followAuthId) {
        this.id = id;
        this.userid = userid;
        this.nickname = nickname;
        this.remarkInfo = remarkInfo;
        this.avatar = avatar;
        this.status = status;
        this.followAuthId = followAuthId;
    }

    public String getRemarkInfo() {
        return remarkInfo;
    }

    public void setRemarkInfo(String remarkInfo) {
        this.remarkInfo = remarkInfo;
    }

    public String getFollowAuthId() {
        return followAuthId;
    }

    public void setFollowAuthId(String followAuthId) {
        this.followAuthId = followAuthId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

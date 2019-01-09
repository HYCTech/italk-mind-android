package com.lw.italk.greendao.model;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 * Created by wapchief on 2017/7/27.
 * 接收好友请求的类
 */

@Entity
public class GroupMember {
    @Id(autoincrement = true)
    private Long id;
    @SerializedName(value = "userid",alternate = {"uid"})
    private String userid;
    private String groupid;
    @SerializedName(value = "nickname",alternate = {"username"})
    private String nickname;
    @SerializedName(value = "image",alternate = {"avatar"})
    private String image;
    private long timestamp;
    @SerializedName(value = "groupnickname",alternate = {"remark"})
    private String groupnickname;
    private boolean disturb;
    private boolean msgsettop;
    private boolean showname;
    @Generated(hash = 1646967818)
    public GroupMember(Long id, String userid, String groupid, String nickname,
            String image, long timestamp, String groupnickname, boolean disturb,
            boolean msgsettop, boolean showname) {
        this.id = id;
        this.userid = userid;
        this.groupid = groupid;
        this.nickname = nickname;
        this.image = image;
        this.timestamp = timestamp;
        this.groupnickname = groupnickname;
        this.disturb = disturb;
        this.msgsettop = msgsettop;
        this.showname = showname;
    }
    @Generated(hash = 1668463032)
    public GroupMember() {
    }
    public String getUserid() {
        return this.userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getGroupid() {
        return this.groupid;
    }
    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
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
    public String getGroupnickname() {
        return this.groupnickname;
    }
    public void setGroupnickname(String groupnickname) {
        this.groupnickname = groupnickname;
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
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

}

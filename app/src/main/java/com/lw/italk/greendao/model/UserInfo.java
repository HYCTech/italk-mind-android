package com.lw.italk.greendao.model;

import com.google.gson.annotations.SerializedName;

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
public class UserInfo {
    @Id(autoincrement = false)
    @SerializedName(value = "uid",alternate = {"userId"})
    private String uid;
    private String token;
    private String username;
    private Long sex = Long.valueOf(0);
    private Long birthday;
    private Long allowtype;

    private String companyId;
    private String companyName;

    private Boolean isFriend;
    private String area;
    private String remarks;
    private String location;
    private String signature;
    private Boolean msgsettings;
    private Long lasttimelogin;
    private String cellphone;
    private String avatar;
    private String qrcode;
    private Boolean notifymsg;
    private Boolean notifydetail;
    private Boolean msgsound;
    private Boolean msgshock;
    private Boolean recommendfriend;
    private Boolean freeze;
    private Boolean vip;
    private Boolean iscurrent;
    private Boolean notifyvoice;
    @Generated(hash = 50210045)
    public UserInfo(String uid, String token, String username, Long sex, Long birthday, Long allowtype, String companyId, String companyName, Boolean isFriend, String area, String remarks, String location, String signature, Boolean msgsettings, Long lasttimelogin, String cellphone, String avatar, String qrcode, Boolean notifymsg, Boolean notifydetail, Boolean msgsound, Boolean msgshock, Boolean recommendfriend, Boolean freeze, Boolean vip,
            Boolean iscurrent, Boolean notifyvoice) {
        this.uid = uid;
        this.token = token;
        this.username = username;
        this.sex = sex;
        this.birthday = birthday;
        this.allowtype = allowtype;
        this.companyId = companyId;
        this.companyName = companyName;
        this.isFriend = isFriend;
        this.area = area;
        this.remarks = remarks;
        this.location = location;
        this.signature = signature;
        this.msgsettings = msgsettings;
        this.lasttimelogin = lasttimelogin;
        this.cellphone = cellphone;
        this.avatar = avatar;
        this.qrcode = qrcode;
        this.notifymsg = notifymsg;
        this.notifydetail = notifydetail;
        this.msgsound = msgsound;
        this.msgshock = msgshock;
        this.recommendfriend = recommendfriend;
        this.freeze = freeze;
        this.vip = vip;
        this.iscurrent = iscurrent;
        this.notifyvoice = notifyvoice;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getToken() {
        return this.token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Long getBirthday() {
        return this.birthday;
    }
    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }
    public Long getAllowtype() {
        return this.allowtype;
    }
    public void setAllowtype(Long allowtype) {
        this.allowtype = allowtype;
    }
    public String getLocation() {
        return this.location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public Boolean getMsgsettings() {
        return this.msgsettings;
    }
    public void setMsgsettings(Boolean msgsettings) {
        this.msgsettings = msgsettings;
    }
    public Long getLasttimelogin() {
        return this.lasttimelogin;
    }
    public void setLasttimelogin(Long lasttimelogin) {
        this.lasttimelogin = lasttimelogin;
    }
    public String getCellphone() {
        return this.cellphone;
    }
    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
    public String getQrcode() {
        return this.qrcode;
    }
    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
    public Boolean getNotifymsg() {
        return this.notifymsg;
    }
    public void setNotifymsg(Boolean notifymsg) {
        this.notifymsg = notifymsg;
    }
    public Boolean getNotifydetail() {
        return this.notifydetail;
    }
    public void setNotifydetail(Boolean notifydetail) {
        this.notifydetail = notifydetail;
    }
    public Boolean getMsgsound() {
        return this.msgsound;
    }
    public void setMsgsound(Boolean msgsound) {
        this.msgsound = msgsound;
    }
    public Boolean getMsgshock() {
        return this.msgshock;
    }
    public void setMsgshock(Boolean msgshock) {
        this.msgshock = msgshock;
    }
    public Boolean getRecommendfriend() {
        return this.recommendfriend;
    }
    public void setRecommendfriend(Boolean recommendfriend) {
        this.recommendfriend = recommendfriend;
    }
    public Boolean getFreeze() {
        return this.freeze;
    }
    public void setFreeze(Boolean freeze) {
        this.freeze = freeze;
    }
    public Boolean getVip() {
        return this.vip;
    }
    public void setVip(Boolean vip) {
        this.vip = vip;
    }
    public Boolean getIscurrent() {
        return this.iscurrent;
    }
    public void setIscurrent(Boolean iscurrent) {
        this.iscurrent = iscurrent;
    }
    public Boolean getNotifyvoice() {
        return this.notifyvoice;
    }
    public void setNotifyvoice(Boolean notifyvoice) {
        this.notifyvoice = notifyvoice;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getSex() {
        return sex;
    }

    public void setSex(Long sex) {
        this.sex = sex;
    }

    public Boolean getFriend() {
        return isFriend;
    }

    public void setFriend(Boolean friend) {
        isFriend = friend;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getIsFriend() {
        return this.isFriend;
    }

    public void setIsFriend(Boolean isFriend) {
        this.isFriend = isFriend;
    }
}

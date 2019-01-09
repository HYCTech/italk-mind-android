package com.lw.italk.gson.user;

/**
 * Created by lxm on 2018/8/23.
 */
public class Profile {
    private String userid;
    private String nickname;
    private int gender;
    private int birthday;
    private String location;
    private int allowtype;
    private String selfsignature;
    private String image;
    private boolean msgsettings;
    private int level;
    private long lasttimelogin;
    private String cellphone;
    private String qrcode;
    private boolean notifymsg;
    private boolean notifyvoice;
    private boolean notifydetail;
    private boolean msgsound;
    private boolean msgshock;
    private boolean recommendfriend;
    private boolean freeze;
    private boolean vip;

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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getAllowtype() {
        return allowtype;
    }

    public void setAllowtype(int allowtype) {
        this.allowtype = allowtype;
    }

    public String getSelfsignature() {
        return selfsignature;
    }

    public void setSelfsignature(String selfsignature) {
        this.selfsignature = selfsignature;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isMsgsettings() {
        return msgsettings;
    }

    public void setMsgsettings(boolean msgsettings) {
        this.msgsettings = msgsettings;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getLasttimelogin() {
        return lasttimelogin;
    }

    public void setLasttimelogin(long lasttimelogin) {
        this.lasttimelogin = lasttimelogin;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public boolean isNotifymsg() {
        return notifymsg;
    }

    public void setNotifymsg(boolean notifymsg) {
        this.notifymsg = notifymsg;
    }

    public boolean isNotifyvoice() {
        return notifyvoice;
    }

    public void setNotifyvoice(boolean notifyvoice) {
        this.notifyvoice = notifyvoice;
    }

    public boolean isNotifydetail() {
        return notifydetail;
    }

    public void setNotifydetail(boolean notifydetail) {
        this.notifydetail = notifydetail;
    }

    public boolean isMsgsound() {
        return msgsound;
    }

    public void setMsgsound(boolean msgsound) {
        this.msgsound = msgsound;
    }

    public boolean isMsgshock() {
        return msgshock;
    }

    public void setMsgshock(boolean msgshock) {
        this.msgshock = msgshock;
    }

    public boolean isRecommendfriend() {
        return recommendfriend;
    }

    public void setRecommendfriend(boolean recommendfriend) {
        this.recommendfriend = recommendfriend;
    }

    public boolean isFreeze() {
        return freeze;
    }

    public void setFreeze(boolean freeze) {
        this.freeze = freeze;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }
}

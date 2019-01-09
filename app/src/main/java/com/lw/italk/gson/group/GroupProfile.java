package com.lw.italk.gson.group;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lxm on 2018/8/23.
 */
public class GroupProfile {
    private String groupid;
    @SerializedName(value = "owner_id",alternate = {"masterId"})
    private String owner_id;
    private int type;
    @SerializedName(value = "name",alternate = {"groupName"})
    private String name;
    private String introduction;
    private String notification;
    @SerializedName(value = "currentmembercount",alternate = {"groupNum"})
    private int currentmembercount;
    private String faceurl;
    private int applyjoinoption;
    private boolean freeze;
    private int maxmembercount;
    private String qrcode;

    public boolean isHasCollect() {
        return hasCollect;
    }

    public void setHasCollect(boolean hasCollect) {
        this.hasCollect = hasCollect;
    }

    private boolean hasCollect;

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public int getCurrentmembercount() {
        return currentmembercount;
    }

    public void setCurrentmembercount(int currentmembercount) {
        this.currentmembercount = currentmembercount;
    }

    public String getFaceurl() {
        return faceurl;
    }

    public void setFaceurl(String faceurl) {
        this.faceurl = faceurl;
    }

    public int getApplyjoinoption() {
        return applyjoinoption;
    }

    public void setApplyjoinoption(int applyjoinoption) {
        this.applyjoinoption = applyjoinoption;
    }

    public boolean isFreeze() {
        return freeze;
    }

    public void setFreeze(boolean freeze) {
        this.freeze = freeze;
    }

    public int getMaxmembercount() {
        return maxmembercount;
    }

    public void setMaxmembercount(int maxmembercount) {
        this.maxmembercount = maxmembercount;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
}

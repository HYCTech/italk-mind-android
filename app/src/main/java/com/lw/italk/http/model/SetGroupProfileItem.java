package com.lw.italk.http.model;

/**
 * Created by 喜明 on 2018/9/1.
 */

public class SetGroupProfileItem {
    private int type;
    private String name;
    private String introduction;
    private String notification;
    private String faceurl;
    private int applyjoinoption;
    private boolean freeze;
    private String qrcode;

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

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
}

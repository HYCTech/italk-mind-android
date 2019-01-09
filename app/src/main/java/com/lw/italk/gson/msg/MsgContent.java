package com.lw.italk.gson.msg;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MsgContent {
    private String thumbRemotePath;
    private String  rawRemotePath;
    private int width;
    private int height;
    private int duration;
    @SerializedName(value = "remotepath",alternate = {"remotePath"})
    private String  remotePath;
    private long  msgId;
    private long  recId;
    private int  recType;
    private int  size;
    private String textMsg;

    private String info;//发起人的请求消息
    private String followAuthId;//本次好友添加的业务认证id，有效期为7天
    private boolean agreed;
    private String uid;
    private List<String> uids;//msgType 9,10
    private String avatar;
    private String username;
    private String filename;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAgreed() {
        return agreed;
    }

    public void setAgreed(boolean agreed) {
        this.agreed = agreed;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getFollowAuthId() {
        return followAuthId;
    }

    public void setFollowAuthId(String followAuthId) {
        this.followAuthId = followAuthId;
    }

    public String getThumbRemotePath() {
        return thumbRemotePath;
    }

    public void setThumbRemotePath(String thumbRemotePath) {
        this.thumbRemotePath = thumbRemotePath;
    }

    public String getRawRemotePath() {
        return rawRemotePath;
    }

    public void setRawRemotePath(String rawRemotePath) {
        this.rawRemotePath = rawRemotePath;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public long getRecId() {
        return recId;
    }

    public void setRecId(long recId) {
        this.recId = recId;
    }

    public int getRecType() {
        return recType;
    }

    public void setRecType(int recType) {
        this.recType = recType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getTextMsg() {
        return textMsg;
    }

    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }

    public List<String> getUids() {
        return uids;
    }

    public void setUids(List<String> uids) {
        this.uids = uids;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}

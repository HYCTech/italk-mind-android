package com.lw.italk.gson.msg;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 喜明 on 2018/8/27.
 */
@Entity
public class MsgItem {
    @Id(autoincrement = true)
    private Long id;
    @SerializedName(value = "msgId",alternate = {"msgid"})
    private long msgid;
    @SerializedName(value = "clientMsgId",alternate = {"localid"})
    private long localid;
    @SerializedName(value = "sendUid",alternate = {"userid"})
    private String userid;
    private String fid;
    private String uid;
    private int chattype;
    @SerializedName(value = "msgType",alternate = {"bussinesstype"})
    private int bussinesstype;
    private long timestamp;
    private boolean burn;
    private int burnsecond;
    private int status;
    private String text;
    private int imageformat;
    private int width;
    private int height;
    private String thumburl;
    private String localthumburl;
    private String localurl;
    private String url;
    private int second;
    private String filename;
    private int size;
    private String desc;
    private String latitude;
    private String longtitude;
    private boolean unread;
    private boolean isgroup;
    private int direct;
    private long downid;
    private String localpath;
    private boolean islisten;
    private long orderId;//消息的顺序id信息，后续可以用于排序
    @Transient
    private MsgContent msgContent;
    @Transient
    public int progress = 0;

    public boolean isBurn() {
        return burn;
    }

    public boolean isUnread() {
        return unread;
    }

    public boolean isIsgroup() {
        return isgroup;
    }

    public boolean isIslisten() {
        return islisten;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public MsgContent getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(MsgContent msgContent) {
        this.msgContent = msgContent;
        if (msgContent == null) return;
            this.url = msgContent.getRawRemotePath();
            this.thumburl = msgContent.getThumbRemotePath();
            this.height = msgContent.getHeight();
            this.width = msgContent.getWidth();
            this.second = msgContent.getDuration();
            if (msgContent.getRemotePath() !=null && msgContent.getRemotePath().length()>0) {
                this.url = msgContent.getRemotePath();
            }
            this.size = msgContent.getSize();
            if (msgContent.getRecId() != 0){
            this.fid =  String.valueOf(msgContent.getRecId());
        }
            if (msgContent.getRecType() != 0){
                this.chattype = msgContent.getRecType();
            }
            this.text = msgContent.getTextMsg();
            this.filename = msgContent.getFilename();
    }

    @Generated(hash = 1731807690)
    public MsgItem(Long id, long msgid, long localid, String userid, String fid, String uid,
            int chattype, int bussinesstype, long timestamp, boolean burn, int burnsecond,
            int status, String text, int imageformat, int width, int height, String thumburl,
            String localthumburl, String localurl, String url, int second, String filename,
            int size, String desc, String latitude, String longtitude, boolean unread,
            boolean isgroup, int direct, long downid, String localpath, boolean islisten,
            long orderId) {
        this.id = id;
        this.msgid = msgid;
        this.localid = localid;
        this.userid = userid;
        this.fid = fid;
        this.uid = uid;
        this.chattype = chattype;
        this.bussinesstype = bussinesstype;
        this.timestamp = timestamp;
        this.burn = burn;
        this.burnsecond = burnsecond;
        this.status = status;
        this.text = text;
        this.imageformat = imageformat;
        this.width = width;
        this.height = height;
        this.thumburl = thumburl;
        this.localthumburl = localthumburl;
        this.localurl = localurl;
        this.url = url;
        this.second = second;
        this.filename = filename;
        this.size = size;
        this.desc = desc;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.unread = unread;
        this.isgroup = isgroup;
        this.direct = direct;
        this.downid = downid;
        this.localpath = localpath;
        this.islisten = islisten;
        this.orderId = orderId;
    }

    @Generated(hash = 1877026623)
    public MsgItem() {
    }
    public Long getId() {
        return this.id;
    }

    public long getMsgid() {
        return this.msgid;
    }
    public void setMsgid(long msgid) {
        this.msgid = msgid;
    }
    public long getLocalid() {
        return this.localid;
    }
    public void setLocalid(long localid) {
        this.localid = localid;
    }
    public String getUserid() {
        return this.userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getFid() {
        return this.fid;
    }
    public void setFid(String fid) {
        this.fid = fid;
    }
    public String getUid() {
        return this.uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public int getChattype() {
        return this.chattype;
    }
    public void setChattype(int chattype) {
        this.chattype = chattype;
    }
    public int getBussinesstype() {
        return this.bussinesstype;
    }
    public void setBussinesstype(int bussinesstype) {
        this.bussinesstype = bussinesstype;
    }
    public long getTimestamp() {
        return this.timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public boolean getBurn() {
        return this.burn;
    }
    public void setBurn(boolean burn) {
        this.burn = burn;
    }
    public int getBurnsecond() {
        return this.burnsecond;
    }
    public void setBurnsecond(int burnsecond) {
        this.burnsecond = burnsecond;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        System.out.println("setStatus ========="+status);
        this.status = status;
    }
    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public int getImageformat() {
        return this.imageformat;
    }
    public void setImageformat(int imageformat) {
        this.imageformat = imageformat;
    }
    public int getWidth() {
        return this.width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return this.height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public String getThumburl() {
        return this.thumburl;
    }
    public void setThumburl(String thumburl) {
        this.thumburl = thumburl;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public int getSecond() {
        return this.second;
    }
    public void setSecond(int second) {
        this.second = second;
    }
    public String getFilename() {
        return this.filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public int getSize() {
        return this.size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getLatitude() {
        return this.latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongtitude() {
        return this.longtitude;
    }
    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }
    public boolean getUnread() {
        return this.unread;
    }
    public void setUnread(boolean unread) {
        this.unread = unread;
    }
    public boolean getIsgroup() {
        return this.isgroup;
    }
    public void setIsgroup(boolean isgroup) {
        this.isgroup = isgroup;
    }
    public int getDirect() {
        return this.direct;
    }
    public void setDirect(int direct) {
        this.direct = direct;
    }
    public long getDownid() {
        return this.downid;
    }
    public void setDownid(long downid) {
        this.downid = downid;
    }
    public String getLocalpath() {
        return this.localpath;
    }
    public void setLocalpath(String localpath) {
        this.localpath = localpath;
    }
    public String getLocalthumburl() {
        return this.localthumburl;
    }
    public void setLocalthumburl(String localthumburl) {
        this.localthumburl = localthumburl;
    }
    public String getLocalurl() {
        return this.localurl;
    }
    public void setLocalurl(String localurl) {
        this.localurl = localurl;
    }
    public boolean getIslisten() {
        return this.islisten;
    }
    public void setIslisten(boolean islisten) {
        this.islisten = islisten;
    }

    public void setId(Long id) {
        this.id = id;
    }
   
}

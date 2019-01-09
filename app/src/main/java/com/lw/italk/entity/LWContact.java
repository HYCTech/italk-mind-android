package com.lw.italk.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 * Created by Administrator on 2018/8/16 0016.
 */

public class LWContact implements Parcelable {
    protected String eid;
    protected String username;
    protected String nick;
    public static final Creator<LWContact> CREATOR = new Creator<LWContact>() {
        public LWContact createFromParcel(Parcel var1) {
            return new LWContact(var1);
        }

        public LWContact[] newArray(int var1) {
            return new LWContact[var1];
        }
    };

    protected LWContact() {
    }

    public LWContact(String var1, String var2) {
        this.eid = var1;
        if(var2.contains("@")) {
//            this.username = EMContactManager.getUserNameFromEid(var2);
            this.username = "测试";
        } else {
            this.username = var2;
        }

    }

    public LWContact(String var1) {
        if(var1.contains("@")) {
            this.eid = var1;
//            this.username = EMContactManager.getUserNameFromEid(var1);
            this.username = "测试";
        } else {
            this.username = var1;
//            this.eid = EMContactManager.getEidFromUserName(var1);
            this.eid = "12";
        }

    }

    public void setUsername(String var1) {
        this.username = var1;
    }

    public String getUsername() {
        return this.username;
    }

    public void setNick(String var1) {
        this.nick = var1;
    }

    public String getNick() {
        return this.nick == null?this.username:this.nick;
    }

    public int compare(LWContact var1) {
        return this.getNick().compareTo(var1.getNick());
    }

    public String toString() {
        return "<contact jid:" + this.eid + ", username:" + this.username + ", nick:" + this.nick + ">";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel var1, int var2) {
        var1.writeString(this.eid);
        var1.writeString(this.username);
    }

    private LWContact(Parcel var1) {
        this.eid = var1.readString();
        this.username = var1.readString();
    }

    public String getEid() {
        return this.eid;
    }

    public void setEid(String var1) {
        this.eid = var1;
    }
}

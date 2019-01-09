package com.lw.italk.gson.group;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by lxm on 2018/8/23.
 */
@Entity
public class GroupItem {
    @Id(autoincrement = false)
    @SerializedName(value = "groupid",alternate = {"gid"})
    private String groupid;
    @SerializedName(value = "name",alternate = {"groupName"})
    private String name;
    private String faceurl;
    private long local;
    private String jid;
    @Generated(hash = 1874034777)
    public GroupItem(String groupid, String name, String faceurl, long local,
            String jid) {
        this.groupid = groupid;
        this.name = name;
        this.faceurl = faceurl;
        this.local = local;
        this.jid = jid;
    }
    @Generated(hash = 7721114)
    public GroupItem() {
    }
    public String getGroupid() {
        return this.groupid;
    }
    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFaceurl() {
        return this.faceurl;
    }
    public void setFaceurl(String faceurl) {
        this.faceurl = faceurl;
    }
    public long getLocal() {
        return this.local;
    }
    public void setLocal(long local) {
        this.local = local;
    }
    public String getJid() {
        return this.jid;
    }
    public void setJid(String jid) {
        this.jid = jid;
    }

}

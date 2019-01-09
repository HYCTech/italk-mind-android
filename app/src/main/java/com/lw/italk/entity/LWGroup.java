package com.lw.italk.entity;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2018/8/19 0019.
 */

public class LWGroup extends LWContact {
    protected String description;
    protected String owner;
    protected ArrayList<String> members;
    protected long lastModifiedTime;
    protected boolean isPublic;
    protected boolean allowInvites;
    protected boolean membersOnly;
    protected int maxUsers = 0;
    protected int affiliationsCount = -1;
    protected boolean isMsgBlocked = false;

    public LWGroup(String var1) {
        this.username = var1;
//        this.eid = EMContactManager.getEidFromGroupId(var1);
        this.lastModifiedTime = 0L;
        this.members = new ArrayList();
        this.description = "";
        this.isPublic = false;
        this.owner = "";
        this.allowInvites = false;
        this.membersOnly = false;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String var1) {
        this.description = var1;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String var1) {
        this.owner = var1;
    }

    public synchronized void addMember(String var1) {
        this.members.add(var1);
    }

    public synchronized void removeMember(String var1) {
        this.members.remove(var1);
    }

    public synchronized List<String> getMembers() {
        return Collections.unmodifiableList(this.members);
    }

    public synchronized void setMembers(List<String> var1) {
        this.members.addAll(var1);
    }

    public String getGroupId() {
        return this.username;
    }

    public void setGroupId(String var1) {
        this.username = var1;
    }

    public String getGroupName() {
        return this.nick;
    }

    public void setGroupName(String var1) {
        this.nick = var1;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public void setIsPublic(boolean var1) {
        this.isPublic = var1;
    }

    public boolean isAllowInvites() {
        return this.allowInvites;
    }

    public boolean isMembersOnly() {
        return this.membersOnly;
    }

    public int getMaxUsers() {
        return this.maxUsers;
    }

    public void setMaxUsers(int var1) {
        this.maxUsers = var1;
    }

    public int getAffiliationsCount() {
        return this.affiliationsCount;
    }

    public void setAffiliationsCount(int var1) {
        this.affiliationsCount = var1;
    }

    public boolean getMsgBlocked() {
        return this.isMsgBlocked;
    }

    public boolean isMsgBlocked() {
        return this.isMsgBlocked;
    }

    public void setMsgBlocked(boolean var1) {
        this.isMsgBlocked = var1;
    }

    public String toString() {
        return this.nick;
    }

    Bitmap getGroupAvator() {
        Exception var1 = new Exception("group avator not supported yet");
        var1.printStackTrace();
        return null;
    }

    void copyGroup(LWGroup var1) {
        this.eid = var1.eid;
        this.description = var1.description;
        this.isPublic = var1.isPublic;
        this.allowInvites = var1.allowInvites;
        this.membersOnly = var1.membersOnly;
        this.lastModifiedTime = System.currentTimeMillis();
        this.members.clear();
        this.members.addAll(var1.getMembers());
        this.nick = var1.nick;
        this.owner = var1.owner;
        this.username = var1.username;
        this.maxUsers = var1.maxUsers;
        this.affiliationsCount = var1.affiliationsCount;
        this.isMsgBlocked = var1.isMsgBlocked;
    }

    public long getLastModifiedTime() {
        return this.lastModifiedTime;
    }

    public void setLastModifiedTime(long var1) {
        this.lastModifiedTime = var1;
    }

    public void setPublic(boolean var1) {
        this.isPublic = var1;
    }
}
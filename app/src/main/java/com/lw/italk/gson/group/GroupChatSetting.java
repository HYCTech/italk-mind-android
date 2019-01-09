package com.lw.italk.gson.group;

/**
 * Created by lxm on 2018/8/23.
 */
public class GroupChatSetting {
    private String groupid;
    private String groupnickname;
    private boolean msgsettop;
    private boolean disturb;
    private boolean shownick;
    private boolean blacklisst;
    private boolean shutup;
    private int shutupuntil;

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupnickname() {
        return groupnickname;
    }

    public void setGroupnickname(String groupnickname) {
        this.groupnickname = groupnickname;
    }

    public boolean isMsgsettop() {
        return msgsettop;
    }

    public void setMsgsettop(boolean msgsettop) {
        this.msgsettop = msgsettop;
    }

    public boolean isDisturb() {
        return disturb;
    }

    public void setDisturb(boolean disturb) {
        this.disturb = disturb;
    }

    public boolean isShownick() {
        return shownick;
    }

    public void setShownick(boolean shownick) {
        this.shownick = shownick;
    }

    public boolean isBlacklisst() {
        return blacklisst;
    }

    public void setBlacklisst(boolean blacklisst) {
        this.blacklisst = blacklisst;
    }

    public boolean isShutup() {
        return shutup;
    }

    public void setShutup(boolean shutup) {
        this.shutup = shutup;
    }

    public int getShutupuntil() {
        return shutupuntil;
    }

    public void setShutupuntil(int shutupuntil) {
        this.shutupuntil = shutupuntil;
    }
}

package com.lw.italk.greendao.model;

public class GroupMembe {
   private String[] memberids;
   private String command;
    private long localid;
    private String userid;
    private String fid;
    private int chattype;
    private int bussinesstype;

    public String[] getMemberids() {
        return memberids;
    }

    public void setMemberids(String[] memberids) {
        this.memberids = memberids;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public long getLocalid() {
        return localid;
    }

    public void setLocalid(long localid) {
        this.localid = localid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public int getChattype() {
        return chattype;
    }

    public void setChattype(int chattype) {
        this.chattype = chattype;
    }

    public int getBussinesstype() {
        return bussinesstype;
    }

    public void setBussinesstype(int bussinesstype) {
        this.bussinesstype = bussinesstype;
    }
}

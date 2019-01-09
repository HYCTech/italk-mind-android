package com.lw.italk.gson.user;

/**
 * Created by lxm on 2018/8/23.
 */
public class Logout {

    private String userid;
    private long timelogout;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public long getTimelogout() {
        return timelogout;
    }

    public void setTimelogout(long timelogout) {
        this.timelogout = timelogout;
    }
}

package com.lw.italk.gson.user;

/**
 * Created by lxm on 2018/8/23.
 */
public class Login {

    private String userid;
    private String nickname;
    private int gender;
    private String image;
    private String token;
    private double lasttimelogin;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double getLasttimelogin() {
        return lasttimelogin;
    }

    public void setLasttimelogin(double lasttimelogin) {
        this.lasttimelogin = lasttimelogin;
    }
}

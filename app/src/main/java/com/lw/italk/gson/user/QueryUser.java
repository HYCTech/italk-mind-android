package com.lw.italk.gson.user;

/**
 * Created by lxm on 2018/8/23.
 */
public class QueryUser {

    private String userid;
    private String nickname;
    private String remarkname;
    private int gender;
    private String image;
    private String phonenumber;
    private int relation;
    private int user_exists;

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

    public String getRemarkname() {
        return remarkname;
    }

    public void setRemarkname(String remarkname) {
        this.remarkname = remarkname;
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

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public int getUserExists() {
        return user_exists;
    }

    public void setUserExists(int user_exists) {
        this.user_exists = user_exists;
    }
}

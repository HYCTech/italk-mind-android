package com.lw.italk.http.model;

/**
 * Created by 喜明 on 2018/8/24.
 */

public class LoginRequest {

    private String userid;
    private String verificationcode;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getVerificationcode() {
        return verificationcode;
    }

    public void setVerificationcode(String verificationcode) {
        this.verificationcode = verificationcode;
    }
}

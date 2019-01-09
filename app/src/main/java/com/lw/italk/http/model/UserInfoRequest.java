package com.lw.italk.http.model;


/**
 * Created by Administrator on 17-8-24.
 */

public class UserInfoRequest {

    private String uid;
    private String tokenId;
    private String account;

    public String getUid() {
        return uid;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}

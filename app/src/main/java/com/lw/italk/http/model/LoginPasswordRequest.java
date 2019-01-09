package com.lw.italk.http.model;

public class LoginPasswordRequest {
    private String account;
    private String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getVerificationcode() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

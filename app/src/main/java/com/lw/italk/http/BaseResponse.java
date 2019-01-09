package com.lw.italk.http;

import com.lw.italk.gson.Meta;

/**
 * 回调信息统一封装类
 * Created by WZG on 2016/7/16.
 */
public class BaseResponse<T> {
    //  判断标示
    private int code;
    private String message;
    //显示数据（用户需要关心的数据）
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

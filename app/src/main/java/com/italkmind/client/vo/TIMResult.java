/**  
* Copyright © 2010 - 2018 All Rights Reserved Powered by italkmind
* @Title: AbstractProtocolBodyParse.java
* @version V1.0  
*/
package com.italkmind.client.vo;


/**
* @ClassName: TIMResult
* @Description: TODO(这里用一句话描述这个类的作用)
* @author fern
* @date 2018年9月6日
*
*/

public class TIMResult<T> {
    private int code = -1;
    private String message;
    private T data;

    public TIMResult() {
    }

    public boolean isSuccess() {
        return this.code == 0;
    }

    public boolean isError() {
        return this.code != 0;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

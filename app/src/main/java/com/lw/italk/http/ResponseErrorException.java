package com.lw.italk.http;

/**
 * Created by cts on 2017/2/13.
 */
public class ResponseErrorException extends Exception {
    private int errorCode;
    private Object data;

    public ResponseErrorException(String msg, int errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    public ResponseErrorException(Object data, String msg, int errorCode) {
        super(msg);
        this.errorCode = errorCode;
        this.data = data;
    }

    public ResponseErrorException(String msg) {
        super(msg);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

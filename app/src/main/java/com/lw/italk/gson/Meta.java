package com.lw.italk.gson;

/**
 * Created by lxm on 2018/8/22.
 */
public class Meta {

    private boolean actionstatus;
    private String errorinfo;
    private int errorcode;
    private String errordisplay;
    private String methodname;

    public boolean isActionstatus() {
        return actionstatus;
    }

    public void setActionstatus(boolean actionstatus) {
        this.actionstatus = actionstatus;
    }

    public String getErrorinfo() {
        return errorinfo;
    }

    public void setErrorinfo(String errorinfo) {
        this.errorinfo = errorinfo;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrordisplay() {
        return errordisplay;
    }

    public void setErrordisplay(String errordisplay) {
        this.errordisplay = errordisplay;
    }

    public String getMethodname() {
        return methodname;
    }

    public void setMethodname(String methodname) {
        this.methodname = methodname;
    }
}

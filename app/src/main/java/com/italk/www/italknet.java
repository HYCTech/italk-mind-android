package com.italk.www;

import android.util.Log;

import com.lw.italk.framework.common.LWJNIManager;

public class italknet  {
//    static {
//        System.loadLibrary("italknet-lib");
//    }
    public native void init();
    public native void setIp(String ip);
    public native void setPort(String port);
    public native boolean connect();
    public native boolean write(String info);
    public native boolean close();
    public native boolean isOpen();
    public static void readcallback(String s) {
        Log.e("123qwe", "readcallback:" + s);
        LWJNIManager.getInstance().readCallBack(s);
    }
    public static void writecallback(int len){
        Log.e("123qwe", "writecallback:" + len);
    }
    public static void connectcallback(boolean connected){
        Log.e("123qwe", "connectcallback:" + connected);
        LWJNIManager.getInstance().connectcallback(connected);
    }
    public static void closecallback(){
        Log.e("123qwe", "closecallback:");
        LWJNIManager.getInstance().closeCallback();
    }

}

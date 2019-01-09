package com.lw.italk.utils;

import android.util.Log;

import com.lw.italk.http.AppConfig;
/**
 * 日志工具类
 * Created by 喜明 on 2018/8/12.
 */

public class ItalkLog {

    private static final String TAG = "lxm";

    public static void v(String msg) {
        if (AppConfig.isDebug()) {
            Log.v(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (AppConfig.isDebug()) {
            o(msg);
        }
    }

    public static void i(String msg) {
        if (AppConfig.isDebug()) {
            o(msg);
        }
    }

    public static void w(String msg) {
        if (AppConfig.isDebug()) {
            Log.w(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (AppConfig.isDebug()) {
            Log.e(TAG, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (AppConfig.isDebug()) {
            o(msg);
        }
    }

    public static void d(String tag, String msg) {
        if (AppConfig.isDebug()) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (AppConfig.isDebug()) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (AppConfig.isDebug()) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (AppConfig.isDebug()) {
            Log.e(tag, msg);
        }
    }

    public static void o(String msg) {
        Log.i(TAG, createMessage(msg));
    }

    private static String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (null == sts) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(ItalkLog.class.getName())) {
                continue;
            }
            return "[" + Thread.currentThread().getName() + "(" + Thread.currentThread().getId()
                    + "):" + st.getFileName() + ":" + st.getLineNumber() + "]";
        }
        return null;
    }

    private static String createMessage(String msg) {
        String functionName = getFunctionName();
        String message = (functionName == null ? msg : (functionName + " - " + msg));
        return message;
    }
}

package com.lw.italk.utils;

import android.os.SystemClock;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cts on 2017/7/11.
 */
public enum ServerTimeUtils {

    INSTANCE;

    private boolean mInit = false;
    private long mStart;
    private long mTime;//单位是s

    public long getTime() {
        return mInit ? mTime + SystemClock.elapsedRealtime() - mStart : System.currentTimeMillis();
    }

    public void setTime(int time) {
        mInit = true;
        mStart = SystemClock.elapsedRealtime();
        mTime = time * 1000L;
    }

    public String getDateString() {
        Date d = new Date(getTime());
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        return sf.format(d);
    }
}

package com.lw.italk.utils;

import android.os.CountDownTimer;
import android.widget.TextView;


/*
     * 倒计时
     * paramLong1 ： 倒计时的时间 （单位：秒）
     * paramLong2 ： 每次减少的时间 （单位：秒）
     * paramTextView ： 倒计时的控件
     * id ：消息标识（用来倒计时结束后销毁消息）
	 */
public class CountdownThread extends CountDownTimer {
    private TextView mCountDownView;
    private int messageID = 0;

    public CountdownThread(long paramLong1, long paramLong2, TextView paramTextView, int id) {
        super(paramLong1 * 1000L, paramLong2 * 1000L);
        this.mCountDownView = paramTextView;
        messageID = id;
    }

    public void onFinish() {
        this.mCountDownView.setEnabled(true);
        this.mCountDownView.setText(0 + "");
    }

    public void onTick(long paramLong) {
        this.mCountDownView.setText(paramLong / 1000L + "");
    }
}

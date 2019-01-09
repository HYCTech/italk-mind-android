package com.lw.italk.utils;
import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;


/**
 * Created by 喜明 on 2018/8/23.
 */

public class TipHelper {

    public static void Vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    public static void Vibrate(final Activity activity, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }

}

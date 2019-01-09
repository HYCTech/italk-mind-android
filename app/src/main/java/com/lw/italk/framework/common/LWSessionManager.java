package com.lw.italk.framework.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.lw.italk.App;
import com.lw.italk.entity.LWContact;

/**
 * Created by Administrator on 2018/8/16 0016.
 */

public class LWSessionManager {
    private static final String TAG = "Session";
    private Context appContext = null;
    public LWContact currentUser = null;
    private String lastLoginUser = null;
    private static LWSessionManager instance = new LWSessionManager();
    LWSessionManager() {
    }

    public static synchronized LWSessionManager getInstance(Context context) {
        if(context != null) {
            instance.appContext = context;
        }

        return instance;
    }

    public static synchronized LWSessionManager getInstance() {
        if(instance.appContext == null) {
            instance.appContext = App.getInstance();
        }

        return instance;
    }

    public String getLastLoginUser() {
        if(this.lastLoginUser == null) {
            SharedPreferences var1 = PreferenceManager.getDefaultSharedPreferences(this.appContext);
            this.lastLoginUser = var1.getString("easemob.chat.loginuser", "");
        }

        return this.lastLoginUser;
    }
}

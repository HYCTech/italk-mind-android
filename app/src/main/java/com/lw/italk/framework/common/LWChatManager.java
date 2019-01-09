package com.lw.italk.framework.common;

import android.content.Context;

import com.lw.italk.App;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2018/8/19 0019.
 */

public class LWChatManager {
    private static LWChatManager instance = new LWChatManager();
    private Context applicationContext;
    private WeakReference<MsgClearListen> clearListener;
    private LWChatManager() {
    }

    public static synchronized LWChatManager getInstance() {
        if(instance.applicationContext == null) {
            instance.applicationContext = App.getInstance();
        }
        return instance;
    }

    public void registerClearListener(MsgClearListen listener){
        this.clearListener = new WeakReference<MsgClearListen>(listener);
    }

    public void notifyClearMessage(String conversationID) {
        MsgClearListen listen = this.clearListener.get();
        if (listen != null) {
            listen.clear(conversationID);
        }
    }

}

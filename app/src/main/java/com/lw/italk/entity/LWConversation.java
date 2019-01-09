package com.lw.italk.entity;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2018/8/16 0016.
 */

public class LWConversation {
    public static final String TAG = "conversation";
    List<LWMessage> messages;
    public int unreadMsgCount = 0;
    public String username;
    public boolean isGroup = false;
    public LWContact opposite = null;

    public void setMessages(List<LWMessage> messages) {
        this.messages = messages;
    }

    public LWMessage getLastMessage() {
        return this.messages.size() == 0?null:(LWMessage)this.messages.get(this.messages.size() - 1);
    }

    public List<LWMessage> getAllMessages() {
        return this.messages;
    }

    public boolean isGroup() {
        return this.isGroup;
    }

    public String getUserName() {
        return this.username;
    }
    public LWConversation(String var1) {
        this.username = var1;
        if(this.messages == null) {
            this.messages = Collections.synchronizedList(new ArrayList());
        }

        if(this.unreadMsgCount <= 0) {
            this.unreadMsgCount = 1;
        }

    }
    public int getUnreadMsgCount() {
        if(this.unreadMsgCount < 0) {
            this.unreadMsgCount = 0;
        }

        return this.unreadMsgCount;
    }

    public int getMsgCount() {
        return this.messages.size();
    }

    public LWMessage getMessage(int var1) {
        if(var1 >= this.messages.size()) {
            Log.e("conversation", "outofbound, messages.size:" + this.messages.size());
            return null;
        } else {
            LWMessage var2 = (LWMessage)this.messages.get(var1);
            if(var2 != null && var2.unread) {
                var2.unread = false;
                if(this.unreadMsgCount > 0) {
                    --this.unreadMsgCount;
//                    this.saveUnreadMsgCount(this.unreadMsgCount);
                }
            }

            return var2;
        }
    }

    public void addMessage(LWMessage var1) {
        if(var1.getChatType() == LWMessage.ChatType.GroupChat) {
            this.isGroup = true;
        }

        if(this.messages.size() > 0) {
            LWMessage var2 = (LWMessage)this.messages.get(this.messages.size() - 1);
            if(var1.getMsgId() != null && var2.getMsgId() != null && var1.getMsgId().equals(var2.getMsgId())) {
                return;
            }
        }

        boolean var5 = false;
        Iterator var4 = this.messages.iterator();

        while(var4.hasNext()) {
            LWMessage var3 = (LWMessage)var4.next();
            if(var3.getMsgId().equals(var1.getMsgId())) {
                var5 = true;
                break;
            }
        }

        if(!var5) {
            this.messages.add(var1);
            if(var1.direct == LWMessage.Direct.RECEIVE && var1.unread) {
                ++this.unreadMsgCount;
//                this.saveUnreadMsgCount(this.unreadMsgCount);
            }
        }

    }

    public List<LWMessage> loadMoreMsgFromDB(String var1, int var2) {
//        new ArrayList();
//        List var3 = c.a().b(this.username, var1, var2);
//        this.messages.addAll(0, var3);
//        Iterator var5 = var3.iterator();
//
//        while(var5.hasNext()) {
//            LWMessage var4 = (LWMessage)var5.next();
////            EMChatManager.getInstance().addMessage(var4, false);
//        }


        return null;
    }

    public List<LWMessage> loadMoreGroupMsgFromDB(String var1, int var2) {
//        List var3 = c.a().a(this.username, var1, var2);
//        this.messages.addAll(0, var3);
//        Iterator var5 = var3.iterator();
//
//        while(var5.hasNext()) {
//            LWMessage var4 = (LWMessage)var5.next();
//            EMChatManager.getInstance().addMessage(var4, false);
//        }

        return null;
    }
}

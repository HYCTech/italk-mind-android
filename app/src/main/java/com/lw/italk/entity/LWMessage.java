package com.lw.italk.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.lw.italk.utils.Utils;
import com.lw.italk.framework.common.LWSessionManager;

import java.util.Hashtable;

/**
 * Created by Administrator on 2018/8/16 0016.
 */

public class LWMessage implements Parcelable, Cloneable {
    private static final String TAG = "msg";
    public LWMessage.Type type;
    public LWMessage.Direct direct;
    public LWMessage.Status status;
    public LWContact from;
    public LWContact to;
    public MessageBody body;
    public String msgId;
    public boolean isAcked;
    public boolean isDelivered;
    public long msgTime;
    public LWMessage.ChatType chatType;
    public transient int progress;
    public Hashtable<String, Object> attributes;
    public transient boolean unread;
    public transient boolean offline;
    public boolean isListened;
    static final String ATTR_ENCRYPTED = "isencrypted";
    public static final Creator<LWMessage> CREATOR = new Creator<LWMessage>() {
        public LWMessage createFromParcel(Parcel var1) {
            return new LWMessage(var1);
        }

        public LWMessage[] newArray(int var1) {
            return new LWMessage[var1];
        }
    };

    public LWMessage(LWMessage.Type var1) {
        this.status = LWMessage.Status.CREATE;
        this.isAcked = false;
        this.isDelivered = false;
        this.chatType = LWMessage.ChatType.Chat;
        this.progress = 0;
        this.attributes = new Hashtable();
        this.unread = true;
        this.offline = false;
        this.type = var1;
        this.msgTime = System.currentTimeMillis();
    }

    public LWMessage.Type getType() {
        return this.type;
    }

    public MessageBody getBody() {
        return this.body;
    }

    public long getMsgTime() {
        return this.msgTime;
    }

    public void setMsgTime(long var1) {
        this.msgTime = var1;
    }

    public static LWMessage createSendMessage(LWMessage.Type var0) {
        LWMessage var1 = new LWMessage(var0);
        var1.direct = LWMessage.Direct.SEND;
        LWContact userContact = LWSessionManager.getInstance().currentUser;
        if(userContact == null) {
            String var3 = LWSessionManager.getInstance().getLastLoginUser();
//            userContact = EMContactManager.getInstance().getContactByUserName(var3);
        }

        var1.from = userContact;
        var1.setMsgId(Utils.getUniqueMessageId());
        return var1;
    }

    public static LWMessage createReceiveMessage(LWMessage.Type var0) {
        LWMessage var1 = new LWMessage(var0);
        var1.direct = LWMessage.Direct.RECEIVE;
        var1.to = LWSessionManager.getInstance().currentUser;
        return var1;
    }

    public void addBody(MessageBody var1) {
        this.body = var1;
    }

    public String getFrom() {
        return this.from == null?null:this.from.username;
    }

    public void setFrom(String var1) {
        LWContact var2 = new LWContact();
        var2.setUsername(var1);
        this.from = var2;
    }

    public void setTo(String var1) {
        LWContact var2 = new LWContact();
        var2.setUsername(var1);
        this.to = var2;
    }

    public String getTo() {
        return this.to == null?null:this.to.username;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public void setMsgId(String var1) {
        this.msgId = var1;
    }

    public void setReceipt(String var1) {
//        EMContactManager var2 = EMContactManager.getInstance();
//        LWContact var3 = null;
//        if(var1.contains("@")) {
////            EMLog.e("msg", "error wrong uesrname format:" + var1);
//        } else {
//            var3 = var2.getContactByUserName(var1);
//        }
//
//        if(var3 == null) {
//            var3 = new LWContact(var1);
//            var2.addContactInternal(var3);
//        }
//
//        this.to = var3;
    }

    public String toString() {
        StringBuffer var1 = new StringBuffer();
        var1.append("msg{from:" + this.from.username);
        var1.append(", to:" + this.to.username);
        var1.append(" body:" + this.body.toString());
        return var1.toString();
    }

    public void setAttribute(String var1, boolean var2) {
        if(this.attributes == null) {
            this.attributes = new Hashtable();
        }

        this.attributes.put(var1, new Boolean(var2));
    }

    public void setAttribute(String var1, int var2) {
        if(this.attributes == null) {
            this.attributes = new Hashtable();
        }

        this.attributes.put(var1, new Integer(var2));
    }

    public void setAttribute(String var1, String var2) {
        if(this.attributes == null) {
            this.attributes = new Hashtable();
        }

        this.attributes.put(var1, var2);
    }

    public boolean getBooleanAttribute(String var1) {
        Boolean var2 = null;
        if(this.attributes != null) {
            var2 = (Boolean)this.attributes.get(var1);
        }

        if(var2 == null) {
//            throw new EaseMobException("attribute " + var1 + " not found");
            return false;
        } else {
            return var2.booleanValue();
        }
    }

    public boolean getBooleanAttribute(String var1, boolean var2) {
        if(this.attributes == null) {
            return var2;
        } else {
            Boolean var3 = (Boolean)this.attributes.get(var1);
            return var3 == null?var2:var3.booleanValue();
        }
    }

    public int getIntAttribute(String var1, int var2) {
        Integer var3 = null;
        if(this.attributes != null) {
            var3 = (Integer)this.attributes.get(var1);
        }

        return var3 == null?var2:var3.intValue();
    }

    public int getIntAttribute(String var1)  {
        Integer var2 = null;
        if(this.attributes != null) {
            var2 = (Integer)this.attributes.get(var1);
        }

        if(var2 == null) {
//            throw new EaseMobException("attribute " + var1 + " not found");
            return -1;
        } else {
            return var2.intValue();
        }
    }

    public String getStringAttribute(String var1)  {
        String var2 = null;
        if(this.attributes != null) {
            var2 = (String)this.attributes.get(var1);
        }

//        if(var2 == null) {
//            throw new EaseMobException("attribute " + var1 + " not found");
//        } else {
//            return var2;
//        }
        return var2;
    }

    public String getStringAttribute(String var1, String var2) {
        String var3 = null;
        if(this.attributes != null) {
            var3 = (String)this.attributes.get(var1);
        }

        return var3 == null?var2:var3;
    }

    public LWMessage.ChatType getChatType() {
        return this.chatType;
    }

    public void setChatType(LWMessage.ChatType var1) {
        this.chatType = var1;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel var1, int var2) {
        var1.writeString(this.type.name());
        var1.writeString(this.direct.name());
        var1.writeString(this.msgId);
        var1.writeLong(this.msgTime);
        var1.writeMap(this.attributes);
        var1.writeParcelable(this.from, var2);
        var1.writeParcelable(this.to, var2);
        var1.writeParcelable(this.body, var2);
    }

    private LWMessage(Parcel var1) {
        this.status = LWMessage.Status.CREATE;
        this.isAcked = false;
        this.isDelivered = false;
        this.chatType = LWMessage.ChatType.Chat;
        this.progress = 0;
        this.attributes = new Hashtable();
        this.unread = true;
        this.offline = false;
        this.type = LWMessage.Type.valueOf(var1.readString());
        this.direct = LWMessage.Direct.valueOf(var1.readString());
        this.msgId = var1.readString();
        this.msgTime = var1.readLong();
        this.attributes = new Hashtable();
        var1.readMap(this.attributes, (ClassLoader)null);
        this.from = (LWContact)var1.readParcelable(LWMessage.class.getClassLoader());
        this.to = (LWContact)var1.readParcelable(LWMessage.class.getClassLoader());
        this.body = (MessageBody)var1.readParcelable(LWMessage.class.getClassLoader());
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean isAcked() {
        return this.isAcked;
    }

    public void setAcked(boolean var1) {
        this.isAcked = var1;
    }

    public boolean isDelivered() {
        return this.isDelivered;
    }

    public void setDelivered(boolean var1) {
        this.isDelivered = var1;
    }

    public boolean isUnread() {
        return this.unread;
    }

    public void setUnread(boolean var1) {
        this.unread = var1;
    }

    public void setType(LWMessage.Type var1) {
        this.type = var1;
    }

    public boolean isListened() {
        return this.isListened;
    }

    public void setListened(boolean var1) {
        this.isListened = var1;
    }

    public static enum ChatType {
        Chat,
        GroupChat;

        private ChatType() {
        }
    }

    public static enum Direct {
        SEND,
        RECEIVE;

        private Direct() {
        }
    }

    public static enum Status {
        SUCCESS,
        FAIL,
        INPROGRESS,
        CREATE;

        private Status() {
        }
    }

    public static enum Type {
        TXT,
        IMAGE,
        VIDEO,
        LOCATION,
        VOICE,
        FILE,
        CMD;

        private Type() {
        }
    }
}

package com.lw.italk.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.File;

/**
 * Created by Administrator on 2018/8/19 0019.
 */

public class VoiceMessageBody extends FileMessageBody implements Parcelable {
    int length;
    public static final Creator<VoiceMessageBody> CREATOR = new Creator<VoiceMessageBody>() {
        public VoiceMessageBody createFromParcel(Parcel var1) {
            return new VoiceMessageBody(var1);
        }

        public VoiceMessageBody[] newArray(int var1) {
            return new VoiceMessageBody[var1];
        }
    };

    public VoiceMessageBody(File var1, int var2) {
        this.length = 0;
        this.localUrl = var1.getAbsolutePath();
        this.fileName = var1.getName();
        this.length = var2;
        Log.d("voicemsg", "create voice, message body for:" + var1.getAbsolutePath());
    }

    VoiceMessageBody(String var1, String var2, int var3) {
        this.length = 0;
        this.fileName = var1;
        this.remoteUrl = var2;
        this.length = var3;
    }

    public int getLength() {
        return this.length;
    }

    public String toString() {
        return "voice:" + this.fileName + ",localurl:" + this.localUrl + ",remoteurl:" + this.remoteUrl + ",length:" + this.length;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel var1, int var2) {
        var1.writeString(this.fileName);
        var1.writeString(this.localUrl);
        var1.writeString(this.remoteUrl);
        var1.writeInt(this.length);
    }

    private VoiceMessageBody(Parcel var1) {
        this.length = 0;
        this.fileName = var1.readString();
        this.localUrl = var1.readString();
        this.remoteUrl = var1.readString();
        this.length = var1.readInt();
    }
}

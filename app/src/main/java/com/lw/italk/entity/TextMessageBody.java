package com.lw.italk.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/8/18 0018.
 */

public class TextMessageBody extends MessageBody implements Parcelable {
    String message;
    public static final Creator<TextMessageBody> CREATOR = new Creator<TextMessageBody>() {
        public TextMessageBody createFromParcel(Parcel var1) {
            return new TextMessageBody(var1);
        }

        public TextMessageBody[] newArray(int var1) {
            return new TextMessageBody[var1];
        }
    };

    public TextMessageBody(String var1) {
        this.message = var1;
    }

    public String getMessage() {
        return this.message;
    }

    public String toString() {
        return "txt:\"" + this.message + "\"";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel var1, int var2) {
        var1.writeString(this.message);
    }

    private TextMessageBody(Parcel var1) {
        this.message = var1.readString();
    }
}
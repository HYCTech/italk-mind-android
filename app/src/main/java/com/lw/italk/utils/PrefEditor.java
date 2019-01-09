package com.lw.italk.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import java.util.Set;


public class PrefEditor {
    private static final String PUBLIC_PREF_FILE = "public";
    private static final String PRIVATE_PREF_FILE = "private_";

    public static void writePublicString(Context context, String key,
                                         String value) {
        writeString(context, PUBLIC_PREF_FILE, key, value);
    }

    public static String readPublicString(Context context, String key,
                                          String defValue) {
        return readString(context, PUBLIC_PREF_FILE, key, defValue);
    }

    public static void writePublicBoolean(Context context, String key,
                                          boolean value) {
        writeBoolean(context, PUBLIC_PREF_FILE, key, value);
    }

    public static boolean readPublicBoolean(Context context, String key,
                                            boolean value) {
        return readBoolean(context, PUBLIC_PREF_FILE, key, value);
    }


    public static void writePublicInt(Context context, String key, int value) {
        writeInt(context, PUBLIC_PREF_FILE, key, value);
    }

    public static int readPublicInt(Context context, String key, int defValue) {
        return readInt(context, PUBLIC_PREF_FILE, key, defValue);
    }

    public static void clear(Context context) {
        SharedPreferences preference = context.getSharedPreferences(
                getPrivatePrefFile(), Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.clear();
        editor.commit();
    }

    private static String getPrivatePrefFile() {
        //    return PRIVATE_PREF_FILE
        //           + Account.INSTANCE.getUid();
        return PRIVATE_PREF_FILE;
    }

    private static void writeString(Context context, String fileName,
                                    String key, String value) {
        if (TextUtils.isEmpty(fileName)) {
            return;
        }
        SharedPreferences preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static String readString(Context context, String fileName,
                                     String key, String defValue) {
        if (TextUtils.isEmpty(fileName)) {
            return defValue;
        }
        SharedPreferences preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return preference.getString(key, defValue);
    }

    private static void writeInt(Context context, String fileName, String key,
                                 int value) {
        if (TextUtils.isEmpty(fileName)) {
            return;
        }
        SharedPreferences preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private static int readInt(Context context, String fileName, String key,
                               int defValue) {
        if (TextUtils.isEmpty(fileName)) {
            return defValue;
        }
        SharedPreferences preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return preference.getInt(key, defValue);
    }

    private static void writeLong(Context context, String fileName, String key,
                                  long value) {
        if (TextUtils.isEmpty(fileName)) {
            return;
        }
        SharedPreferences preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    private static long readLong(Context context, String fileName, String key,
                                 long defValue) {
        if (TextUtils.isEmpty(fileName)) {
            return defValue;
        }
        SharedPreferences preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return preference.getLong(key, defValue);
    }


    private static void writeBoolean(Context context, String fileName,
                                     String key, boolean value) {
        if (TextUtils.isEmpty(fileName)) {
            return;
        }
        SharedPreferences preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private static boolean readBoolean(Context context, String fileName,
                                       String key, boolean defValue) {
        if (TextUtils.isEmpty(fileName)) {
            return defValue;
        }
        SharedPreferences preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return preference.getBoolean(key, defValue);
    }

    private static void writeStringSet(Context context, String fileName,
                                       String key, Set<String> value) {
        if (TextUtils.isEmpty(fileName)) {
            return;
        }
        SharedPreferences preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }

    private static Set<String> readStringSet(Context context, String fileName,
                                             String key, Set<String> defValue) {
        if (TextUtils.isEmpty(fileName)) {
            return defValue;
        }
        SharedPreferences preference = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return preference.getStringSet(key, defValue);
    }
}

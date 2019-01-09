package com.italkmind.client.vo.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lw.italk.http.BaseResponse;

import java.lang.reflect.Type;

public class MindFileInfo {
    private String thumbPath;
    private String rawPath;

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public void setRawPath(String rawPath) {
        this.rawPath = rawPath;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public String getRawPath() {
        return rawPath;
    }


    public static BaseResponse<MindFileInfo> jsonToMind (String json){
        Type type = new TypeToken<BaseResponse<MindFileInfo>>() {
        }.getType();
//        String body = response.body().string();
        Gson gson = new Gson();
        BaseResponse<MindFileInfo> file = gson.fromJson(json, type);
        return file;
    }
}

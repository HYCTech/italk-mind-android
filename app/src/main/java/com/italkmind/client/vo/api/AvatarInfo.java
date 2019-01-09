package com.italkmind.client.vo.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lw.italk.http.BaseResponse;

import java.lang.reflect.Type;

public class AvatarInfo {
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public static AvatarInfo jsonToAvatar (String json){
        Type type = new TypeToken<BaseResponse<AvatarInfo>>() {
        }.getType();
//        String body = response.body().string();
        Gson gson = new Gson();
        BaseResponse<AvatarInfo> file = gson.fromJson(json, type);
        return file.getData();
    }
}

package com.lw.italk.framework.common;

import android.content.Context;

import com.italkmind.client.vo.api.LoginInfo;
import com.lw.italk.App;
import com.lw.italk.GloableParams;
import com.lw.italk.entity.LWContact;
import com.lw.italk.framework.helper.SharedPrefHelper;
import com.lw.italk.greendao.model.UserInfo;
import com.lw.italk.utils.CommonUtils;
import com.lw.italk.utils.PathUtil;

/**
 * Created by Administrator on 2018/8/16 0016.
 */

public class LWUserManager {
    private static final String TAG = "Session";
    private Context appContext = null;
    public LWContact currentUser = null;
    private String lastLoginUser = null;
    private static LWUserManager instance = null;
    private UserInfo mUserInfo;

    private String token;
    private String fileAuthid;// 文件token信息
    private LoginInfo.LinkServerInfo linkServerInfo;// 链接信息

    LWUserManager() {
    }

    public static synchronized LWUserManager getInstance() {
        if (instance == null) {
            instance = new LWUserManager();
        }
        return instance;
    }

    public UserInfo getUserInfo() {
        if (mUserInfo == null) {
            mUserInfo = LWDBManager.getInstance().getUserInfo();
        }
        return mUserInfo;
    }
    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
       if(userInfo!=null){
           PathUtil.getInstance().initDirs("user", userInfo.getUid(), App.getInstance());
           LWDBManager.getInstance().insertOrReplace(userInfo);
       }
    }
    public void updateUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
        LWDBManager.getInstance().updateUserInfo(userInfo);
    }
    public String isLogin() {
        UserInfo userInfo = getUserInfo();
        return null == userInfo ? "":userInfo.getUid() ;
    }

    public String getToken() {
        UserInfo userInfo = getUserInfo();
        if (userInfo !=null && userInfo.getToken() != null){
            token = userInfo.getToken();
        }
        if (token == null){
            token = SharedPrefHelper.getInstance().getTokenId();
        }
        return token;
    }

    public String getFileAuthid() {
        if (fileAuthid == null){
            fileAuthid = SharedPrefHelper.getInstance().getFileAuthId();
        }
        return fileAuthid;
    }

    public LoginInfo.LinkServerInfo getLinkServerInfo() {
        if (linkServerInfo == null){
            linkServerInfo = SharedPrefHelper.getInstance().getLinkInfo();
        }
        return linkServerInfo;
    }

    public void updateLinkServerInfo(LoginInfo.LinkServerInfo linkServerInfo) {
        SharedPrefHelper.getInstance().setLinkInfo(linkServerInfo);
        this.linkServerInfo = SharedPrefHelper.getInstance().getLinkInfo();
    }
}

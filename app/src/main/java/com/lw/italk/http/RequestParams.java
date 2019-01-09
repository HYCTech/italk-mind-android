package com.lw.italk.http;

import android.text.TextUtils;

import com.lw.italk.utils.CommonUtils;
import com.lw.italk.utils.ServerTimeUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;


/**
 * Created by cts on 2017/3/10.
 */
public abstract class RequestParams extends HashMap<String, String> {

    public RequestParams() {

//        put(Parameter.TIME, String.valueOf(ServerTimeUtils.INSTANCE.getTime() / 1000));
//        if (AccountManager.INSTANCE.isLogin()) {
//            put(Parameter.TOKEN, AccountManager.INSTANCE.getAccount().getToken());
//        }
        addExtraParams(this);
        if (!AccountManager.INSTANCE.isLogin()) {
//            put(Parameter.SIGN, CommonUtils.getDigestSign(CommonUtils.sortParms(this) + AppConfig.SECURITY_KEY));
        } else {
//            put(Parameter.SIGN, CommonUtils.getDigestSign(CommonUtils.sortParms(this) + AccountManager.INSTANCE.getAccount().getPrivate_key()));
        }
    }

    //这个只有在token登陆的时候使用，其他都是使用上个那个构造函数.
    public RequestParams(String private_key) {

        long curTime = System.currentTimeMillis() / 1000;
        put(Parameter.TIME, String.valueOf(curTime));
        if (AccountManager.INSTANCE.getAccount() != null && !TextUtils.isEmpty(AccountManager.INSTANCE.getAccount().getToken())) {
            put(Parameter.TOKEN, AccountManager.INSTANCE.getAccount().getToken());
        }
        addExtraParams(this);

        put(Parameter.SIGN, CommonUtils.getDigestSign(CommonUtils.sortParms(this) + private_key));

    }

    public abstract void addExtraParams(HashMap<String, String> map);

    public String toUrlString() {
        String url = "";
        for (Entry<String, String> entry : this.entrySet()) {
            if (url.length() > 0) {
                url += "&";
            }
            try {
                url += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return url;
    }
}

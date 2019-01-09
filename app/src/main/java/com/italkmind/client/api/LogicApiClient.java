/**  
* Copyright © 2010 - 2018 All Rights Reserved Powered by italkmind
* @Title: AbstractProtocolBodyParse.java
* @version V1.0  
*/
package com.italkmind.client.api;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.italkmind.client.util.ClientTools;
import com.italkmind.client.vo.TIMResult;
import com.italkmind.client.vo.api.LoginInfo;

/**
 * @ClassName: LogicApiClient
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author fern
 * @date 2018年9月7日
 *
 */

public abstract class LogicApiClient {
    private static Gson gson = new Gson();
    private static final String HOST = "http://47.96.126.173:8887/";
//    private static final String HOST = "http://127.0.0.1:8070/";

    public static LoginInfo login(String account, String password) throws JsonSyntaxException, IOException {
        String url = HOST + "/profile/login";
        Map<String, Object> params = new HashMap<>(2);
        params.put("account", account);
        params.put("password", ClientTools.hashMd5(password));//"e99a18c428cb38d5f260853678922e03"
        @SuppressWarnings("serial")
        Type jsonType = new TypeToken<TIMResult<LoginInfo>>() {}.getType();
        TIMResult<LoginInfo> result = gson.fromJson(ClientTools.readContentFromPost(url, params), jsonType);
        if (result.isError()) {
            ClientTools.msgLog("账号认证失败，具体结果为" + result.toString());
            System.exit(0);
        }
        return result.getData();
    }
}

package com.lw.italk.http;

import android.text.TextUtils;

import com.lw.italk.App;
import com.lw.italk.entity.AccountInfo;
import com.lw.italk.utils.PrefEditor;

import java.util.List;



/**
 * Created by cts on 2017/1/22.
 */
public enum AccountManager {

    INSTANCE;

    private static final String TAG = AccountManager.class.getSimpleName();
    private static final String EMPTY_ACCOUNT_STRING = "";

    private AccountInfo mAccount = AccountInfo.EMPTY;

    public void save(AccountInfo info) {
        if (info == null) {
            info = AccountInfo.EMPTY;
        }

        String account = ThirdLibs.getInstance().getGson().toJson(info);
        PrefEditor.writePublicString(App.getInstance(), Parameter.ACCOUNT_INFO, account);
        this.mAccount = info;
    }

    //退出账号，修改密码，忘记密码登 切换账号相关行为调用。
    public void clear() {
        String name = mAccount != null ? mAccount.getUser_name() : "";
        this.mAccount = AccountInfo.EMPTY;
        mAccount.setUser_name(name);
        PrefEditor.writePublicString(App.getInstance(), Parameter.ACCOUNT_INFO, ThirdLibs.getInstance().getGson().toJson(mAccount));

    }

    //退出app的时候调用。
    public void quit() {
        mAccount = AccountInfo.EMPTY;
    }

    public void emptyAccount() {
        this.mAccount = AccountInfo.EMPTY;
    }

    public AccountInfo construct() {
        String account = PrefEditor.readPublicString(App.getInstance(), Parameter.ACCOUNT_INFO, EMPTY_ACCOUNT_STRING);
        return account.equals(EMPTY_ACCOUNT_STRING) ? AccountInfo.EMPTY : ThirdLibs.getInstance().getGson().fromJson(account, AccountInfo.class);
    }

    public void update(AccountInfo info) {
        this.mAccount = info;
    }

    public AccountInfo getAccount() {
        return mAccount;
    }

    public boolean isLogin() {
        return mAccount != null && !TextUtils.isEmpty(mAccount.getToken());
    }

}

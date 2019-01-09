package com.lw.italk.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.lw.italk.R;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.framework.common.LWConversationManager;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.gson.msg.MsgItem;
import com.lw.italk.gson.user.Logout;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.utils.CommonUtils;
import com.lw.italk.utils.Utils;
import com.zhang.netty_lib.netty.NettyClient;

import java.lang.reflect.Type;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flyn.Eyes;

/**
 * Created by 喜明 on 2018/8/14.
 */

public class UserSettingActivity extends BaseActivity implements Response{
    @BindView(R.id.left_bar_item)
    TextView mLeftBarItem;
    @BindView(R.id.center_bar_item)
    TextView mCenterBarItem;
    @BindView(R.id.title_bar_left)
    LinearLayout titleBarLeft;
    @BindView(R.id.right_title_bar)
    TextView mRightTitleBar;
    @BindView(R.id.title_bar_right)
    RelativeLayout titleBarRight;
    @BindView(R.id.message)
    TextView mMessage;
    @BindView(R.id.privacy)
    TextView mPrivacy;
    @BindView(R.id.about)
    TextView mAbout;
    @BindView(R.id.advise)
    TextView mAdvise;
    @BindView(R.id.empty_chat_record)
    TextView mEmptyChatRecord;
    @BindView(R.id.quit)
    TextView mQuit;

    @Override
    protected int setContentView() {
        return R.layout.activity_user_setting;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mLeftBarItem.setText("我");
        mCenterBarItem.setText("设置");
        mRightTitleBar.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Eyes.translucentStatusBar(this, true);
    }

    private void emptyChatRecord() {
        final Dialog dialog = new Dialog(this, R.style.dialog);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_bottom, null);
        //初始化控件
        TextView mTitle = (TextView) inflate.findViewById(R.id.dialog_title);
        mTitle.setText("将清空所有个人和群聊的聊天记录");
        TextView mEmptyChat = (TextView) inflate.findViewById(R.id.empty_chat);
        mEmptyChat.setText("清空聊天记录");
        mEmptyChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                String uid=LWUserManager.getInstance().getUserInfo().getUid();

                MsgItem msgItem=new MsgItem();
                msgItem.setUserid(uid);
                //LWConversationManager.getInstance().deletConversationAll();
                LWConversationManager.getInstance().deleteConversionByUid(uid);
                LWConversationManager.getInstance().deleteMsgItemByUid(uid);
                //EMChatManager.getInstance().clearConversation(toChatUsername);
            }
        });
        TextView mCencel = (TextView) inflate.findViewById(R.id.cancel);
        mCencel.setText("取消");
        mCencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        if(dialogWindow == null){
            return;
        }
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height  = ViewGroup.LayoutParams.WRAP_CONTENT;
//        lp.y = 20;//设置Dialog距离底部的距离
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    private void quitAccount() {
        final Dialog dialog = new Dialog(this, R.style.dialog);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_bottom, null);
        //初始化控件
        TextView mTitle = (TextView) inflate.findViewById(R.id.dialog_title);
        mTitle.setText("退出后不会删除任何历史记录，下次登入依然可以使用本账户");
        TextView mEmptyChat = (TextView) inflate.findViewById(R.id.empty_chat);
        mEmptyChat.setText("退出登录");
        mEmptyChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                logoutAccount();
            }
        });
        TextView mCencel = (TextView) inflate.findViewById(R.id.cancel);
        mCencel.setText("取消");
        mCencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        if(dialogWindow == null){
            return;
        }
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height  = ViewGroup.LayoutParams.WRAP_CONTENT;
//        lp.y = 20;//设置Dialog距离底部的距离
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    @OnClick({R.id.left_bar_item, R.id.message, R.id.privacy, R.id.about, R.id.advise, R.id.empty_chat_record, R.id.quit})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.left_bar_item:
                finish();
                break;
            case R.id.message:
                intent.setClass(this, SettingMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.privacy:
                intent.setClass(this, SettingPrivacyActivity.class);
                startActivity(intent);
                break;
            case R.id.about:
                intent.setClass(this, SettingAboutActivity.class);
                startActivity(intent);
                break;
            case R.id.advise:
//                ThirdLibs.getInstance().getOssAuthenticationService().get("sts")
//                        .enqueue(new Callback<OssAuthentication>() {
//                            @Override
//                            public void onResponse(Call<OssAuthentication> call, retrofit2.Response<OssAuthentication> response) {
//                                try {
//                                    OssAuthentication body = response.body();
//                                    ItalkLog.e(body.toString());
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    ItalkLog.o("IOException:" + e.getMessage());
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<OssAuthentication> call, Throwable throwable) {
//
//                            }
//                        });
                Toast.makeText(UserSettingActivity.this, "功能暂未开放,敬请期待.",  Toast.LENGTH_SHORT).show();
                break;
            case R.id.empty_chat_record:
                emptyChatRecord();
                break;
            case R.id.quit:
                quitAccount();
                break;
            default:
                break;
        }
    }

    /**
     * 退出登录
     */
    private void logoutAccount(){
        String tokeId = LWUserManager.getInstance().getToken();
        System.out.println("========>"+tokeId);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("tokenId", tokeId);
        HttpUtils.doPostFormMap(this, Request.Path.USER_LOGOUT, map, false, Request.Code.USER_LOGOUT, this);

    }

    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode) {
            case Request.Code.USER_LOGOUT:
                LWDBManager.getInstance().getUserInfo().setIscurrent(false);//账号为退出状态
                LWDBManager.getInstance().updateUserInfo(LWDBManager.getInstance().getUserInfo());
                NettyClient.getInstance().disconnect();
                Intent intent = new Intent(this, AccountLoginActivity.class);
                //跳转后关闭activity之前的所有activity（原理是清理activity堆栈）
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void error(ResponseErrorException t, int requestCode) {
        Utils.start_Activity(UserSettingActivity.this, AccountLoginActivity.class);
    }

    @Override
    public Type getTypeToken(int requestCode) {
        Type type = null;
        switch (requestCode) {
            case Request.Code.USER_LOGOUT:
                type = new TypeToken<BaseResponse<Logout>>() {
                }.getType();
                break;
            default:
                break;
        }
        return type;
    }
}

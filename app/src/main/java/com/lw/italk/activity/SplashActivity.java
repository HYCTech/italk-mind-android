package com.lw.italk.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.framework.common.LWFriendManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.framework.helper.SharedPrefHelper;
import com.lw.italk.utils.CommonUtils;
import com.lw.italk.utils.Constants;
import com.lw.italk.utils.Utils;
import com.lw.italk.view.StatusBarUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SplashActivity extends Activity {

    private static final int REQUEST_ALL_PERMISSION = 0x12;
    private static final int TO_SETTING_APPLYPERMISSION = 1;
    private static final int TO_SETTING_NOTIFICATION = 2;
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        StatusBarUtil.setColor(this, Color.WHITE);
        // initBaiduPush();
        // initData();
        applypermission();
    }

    private void init() {
//        Boolean isLogin = Utils.getBooleanValue(SplashActivity.this,
//                Constants.LoginState);
//        if (isLogin) {
//            // Intent intent = new Intent(this, UpdateService.class);
//            // startService(intent);
//            getLogin();
//        } else {
//            mHandler.sendEmptyMessage(0);
//        }

        String userid = LWUserManager.getInstance().isLogin();
        Intent intent = new Intent();
        Log.e("123qwe", "isLogin:" + userid);
        if (!TextUtils.isEmpty(userid)) {
            intent.setClass(SplashActivity.this, MainActivity.class);
        } else {
            intent.setClass(SplashActivity.this, AccountLoginActivity.class);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        finish();
    }

    private void getLogin() {
        String name = Utils.getValue(this, Constants.User_ID);
        String pwd = Utils.getValue(this, Constants.PWD);
//		if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(name))
        getChatserive(name, pwd);
//		else {
//			Utils.RemoveValue(SplashActivity.this, Constants.LoginState);
//			mHandler.sendEmptyMessageDelayed(0, 600);
//		}
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            String userid = LWUserManager.getInstance().isLogin();
            Intent intent = new Intent();
            Log.e("123qwe", "isLogin:" + userid);
            if (!TextUtils.isEmpty(userid)) {
                intent.setClass(SplashActivity.this, MainActivity.class);
            } else {
                intent.setClass(SplashActivity.this, AccountLoginActivity.class);
            }
            startActivity(intent);
            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            finish();
        }
    };

    private void getChatserive(final String userName, final String password) {
//		EMChatManager.getInstance().login(userName, password, new EMCallBack() {// 回调
//					@Override
//					public void onSuccess() {
//						runOnUiThread(new Runnable() {
//							public void run() {
//								// TODO 保存用户信息
//								Utils.putBooleanValue(SplashActivity.this,
//										Constants.LoginState, true);
//								Utils.putValue(SplashActivity.this,
//										Constants.User_ID, userName);
//								Utils.putValue(SplashActivity.this,
//										Constants.PWD, password);
//
//								Log.e("Token", EMChatManager.getInstance()
//										.getAccessToken());
//								Log.d("main", "登陆聊天服务器成功！");
//								// 加载群组和会话
//								EMGroupManager.getInstance().loadAllGroups();
//								EMChatManager.getInstance()
//										.loadAllConversations();
        mHandler.sendEmptyMessage(0);
//							}
//						});
//					}
//
//					@Override
//					public void onProgress(int progress, String status) {
//
//					}
//
//					@Override
//					public void onError(int code, String message) {
//						Log.d("main", "登陆聊天服务器失败！");
//					}
//				});
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        applypermission();
    }

    public void applypermission() {
        List<String> permissions = new ArrayList<String>() {
            private static final long serialVersionUID = 1L;

            {
                add(Manifest.permission.CAMERA);
                add(Manifest.permission.WRITE_CONTACTS);
                add(Manifest.permission.READ_PHONE_STATE);
                add(Manifest.permission.ACCESS_FINE_LOCATION);
                add(Manifest.permission.READ_EXTERNAL_STORAGE);
                add(Manifest.permission.RECORD_AUDIO);
            }
        };

        for (Iterator<String> permission = permissions.iterator(); permission.hasNext(); ) {
            String s = permission.next();
            if (ContextCompat.checkSelfPermission(SplashActivity.this, s) == PackageManager.PERMISSION_GRANTED) {
                permission.remove();
            }
        }
        String[] requests = new String[permissions.size()];
        permissions.toArray(requests);

        if (requests.length > 0) {
            ActivityCompat.requestPermissions(
                    SplashActivity.this, requests, REQUEST_ALL_PERMISSION);
        } else {
            init();
        }
    }

    private void showMissingPermissionDialog(final int type) {
        final Dialog dialog = new Dialog(this, R.style.dialog);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_bottom, null);
        //初始化控件
        TextView mTitle = (TextView) inflate.findViewById(R.id.dialog_title);
        if (type == TO_SETTING_APPLYPERMISSION) {
            mTitle.setText("请前往设置界面授予权限，否则将退出应用");
        } else if (type == TO_SETTING_NOTIFICATION) {
            mTitle.setText("请前往设置界面开启消息通知权限，否则将无法收到通知消息");
        }
        TextView mEmptyChat = (TextView) inflate.findViewById(R.id.empty_chat);
        mEmptyChat.setText("去设置");
        mEmptyChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (type == TO_SETTING_APPLYPERMISSION) {
                    startAppSettings();
                } else if (type == TO_SETTING_NOTIFICATION) {
                    startToNotification();
                }

            }
        });
        TextView mCencel = (TextView) inflate.findViewById(R.id.cancel);
        if (type == TO_SETTING_APPLYPERMISSION) {
            mCencel.setText("退出");
        } else if (type == TO_SETTING_NOTIFICATION) {
            mCencel.setText("取消");
        }
        mCencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (type == TO_SETTING_APPLYPERMISSION) {
                    finish();
                }else{
                    init();
                }
            }
        });
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        lp.y = 20;//设置Dialog距离底部的距离
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.setCancelable(false);
        dialog.show();//显示对话框
    }

    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions != null) {
            switch (requestCode) {
                case REQUEST_ALL_PERMISSION:
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            showMissingPermissionDialog(TO_SETTING_APPLYPERMISSION);
                            return;
                        }
                    }
                    showMissingPermissionDialog(TO_SETTING_NOTIFICATION);

                    break;
            }
        }
    }


    /**
     * 跳到通知栏设置界面
     */
    private void startToNotification() {
        Intent localIntent = new Intent();
        //直接跳转到应用通知设置的代码：
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            localIntent.putExtra("app_package", getPackageName());
            localIntent.putExtra("app_uid", getApplicationInfo().uid);
        } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
            localIntent.setData(Uri.parse("package:" + getPackageName()));
        } else {
            //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
            }
        }
        startActivity(localIntent);
    }

    /**
     * 获取通知权限
     *
     * @param context
     */
    @SuppressLint("NewApi")
    private boolean isNotificationEnabled(Context context) {

        AppOpsManager mAppOps =
                (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Class appOpsClass = null;

        /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());

            Method checkOpNoThrowMethod =
                    appOpsClass.getMethod(CHECK_OP_NO_THROW,
                            Integer.TYPE, Integer.TYPE, String.class);

            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (Integer) opPostNotificationValue.get(Integer.class);

            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) ==
                    AppOpsManager.MODE_ALLOWED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
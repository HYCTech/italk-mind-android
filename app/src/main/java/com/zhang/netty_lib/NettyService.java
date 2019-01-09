package com.zhang.netty_lib;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.italkmind.client.protocol.ProtocolBodyParseHelper;
import com.italkmind.client.util.ClientTools;
import com.italkmind.client.vo.protocol.ItalkMindMessage;
import com.italkmind.client.vo.protocol.body.SystemMessage;
import com.lw.italk.MyActivityManager;
import com.lw.italk.activity.AccountLoginActivity;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.LWJNIManager;
import com.lw.italk.greendao.model.UserInfo;
import com.zhang.netty_lib.activity.ActivityManager;
import com.zhang.netty_lib.activity.NettyActivity;
import com.zhang.netty_lib.bean.Netty_RegisterInfo;
import com.zhang.netty_lib.bean.NettyBaseFeed;
import com.zhang.netty_lib.netty.NettyClient;
import com.zhang.netty_lib.netty.NettyListener;

import java.lang.ref.WeakReference;
import java.util.Stack;

/**
 *
 */
public class NettyService extends Service implements NettyListener {

    private NetworkReceiver receiver;
    public static final String TAG = NettyService.class.getName();

    public NettyService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        NettyClient.getInstance().setListener(this);
        NettyClient.getInstance().setReconnectNum(5);
        connect();
        return new NettyBinder();
    }
    public class NettyBinder extends Binder {
        public NettyService getService() {
            return NettyService.this;
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NettyClient.getInstance().setReconnectNum(5);
        connect();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        NettyClient.getInstance().setReconnectNum(0);
        NettyClient.getInstance().disconnect();
    }

    private void connect() {
        if (!NettyClient.getInstance().getConnectStatus()) {
            NettyClient.getInstance().setListener(this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NettyClient.getInstance().connect();//连接服务器
                }
            }).start();
        }
    }

    @Override
    public void onMessageResponse(Object messageHolder) {
        notifyData(NettyActivity.MSG_FROM_SERVER, messageHolder);
        LWJNIManager.getInstance().receiveTCPMessage((ItalkMindMessage) messageHolder);
    }

    private void notifyData(int type, Object/*ItalkMindMessage*/ messageHolder) {
//        final Stack<NettyActivity> activities = ActivityManager.getInstance().getActivities();
//        for (NettyActivity activity : activities) {
//            if (activity == null || activity.isFinishing()) {
//                continue;
//            }
//            Message message = Message.obtain();
//            message.what = type;
//            message.obj = messageHolder;
//            activity.getHandler().sendMessage(message);
//        }
    }

    @Override
    public void onServiceStatusConnectChanged(int statusCode) {
        if (statusCode == NettyListener.STATUS_CONNECT_SUCCESS) {
            Log.e(TAG, "connect sucessful");
            LWJNIManager.getInstance().connectcallback(true);
        }else if (statusCode == NettyListener.STATUS_CONNECT_AUTH_FAIL){
            UserInfo userInfo=LWDBManager.getInstance().getUserInfo();
            if(userInfo!=null){
                userInfo.setIscurrent(false);//账号为退出状态
                LWDBManager.getInstance().updateUserInfo(userInfo);
            }
            Activity activity = MyActivityManager.getInstance().getCurrentActivity();
            if (activity != null && activity.getClass().getName().equals(AccountLoginActivity.class.getName())){

            }else {
                Intent intent = new Intent(this, AccountLoginActivity.class);
                //跳转后关闭activity之前的所有activity（原理是清理activity堆栈）
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            this.stopSelf();
        }
        else {
            Log.e(TAG, "connect fail statusCode = " + statusCode);
            notifyData(NettyActivity.MSG_NET_WORK_ERROR, String.valueOf("服务器连接失败"));
        }

    }


    public class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI
                        || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    connect();
                    Log.e(TAG, "connecting ...");
                }
            }
        }
    }


}

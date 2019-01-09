package com.lw.italk.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.italkmind.client.vo.api.MindFileInfo;
import com.lw.italk.activity.AccountLoginActivity;
import com.lw.italk.framework.common.LWConversationManager;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.LWJNIManager;
import com.lw.italk.gson.msg.MsgItem;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.ErrorCode;
import com.lw.italk.http.RetrofitCallback;

import java.io.File;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class FileSendCallback extends RetrofitCallback<ResponseBody> {
    public MsgItem sendMessage;
    public MessageAdapter.ViewHolder viewHolder;

    private Activity activity;
    private WeakReference<MessageAdapter> adapter;

    public FileSendCallback(Activity _activity, MessageAdapter _adapter) {
        this.activity = _activity;
        this.adapter = new WeakReference<>(_adapter);
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.e("123qwe", "onFailure call:" + t.getMessage()+t.getStackTrace());
        t.printStackTrace();
        fileSendFail(this.sendMessage,null);
        MessageAdapter adapter = this.adapter.get();
        if (adapter !=null){
            adapter.fileSendCallBackMap.remove(String.valueOf(sendMessage.getLocalid()));
        }
    }


    @Override
    public void onSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
        try{
            File file = new File(sendMessage.getUrl());

            String body = response.body().string();
            BaseResponse<MindFileInfo> fileRes = MindFileInfo.jsonToMind(body);
            if (fileRes.getCode() == ErrorCode.SUCCESS) {
                MindFileInfo fileInfo = fileRes.getData();
                this.sendMessage.setUrl(fileInfo.getRawPath());
                this.sendMessage.setThumburl(fileInfo.getThumbPath());
                Log.e("TAG", "onSuccessCallback body:" + body);
                this.sendMessage.setFilename(file.getName());
                LWJNIManager.getInstance().sendMsgItem(this.sendMessage);
            }else if (fileRes.getCode() == ErrorCode.FILE_NOT_LOGIN){
                LWDBManager.getInstance().getUserInfo().setIscurrent(false);//账号为退出状态
                LWDBManager.getInstance().updateUserInfo(LWDBManager.getInstance().getUserInfo());
                Intent intent = new Intent(activity, AccountLoginActivity.class);
                //跳转后关闭activity之前的所有activity（原理是清理activity堆栈）
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                activity.finish();
                fileSendFail(this.sendMessage,"上传文件异常，请重新登录");
            }else {
                fileSendFail(this.sendMessage,fileRes.getMessage());
            }
        }catch (Exception e){
            e.printStackTrace();
            fileSendFail(this.sendMessage,null);
        }
        MessageAdapter adapter = this.adapter.get();
        if (adapter !=null){
            adapter.fileSendCallBackMap.remove(String.valueOf(sendMessage.getLocalid()));
        }
    }

    @Override
    public void onLoading(long total, long progress) {
        Log.e("TAG", "sendVideoMessage onProgressCallback progress:" + progress);
        final int pecent = (int) (progress *100 /total);
        this.sendMessage.progress = pecent;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewHolder.tv.setText(pecent + "%");
            }
        });
    }

    private void fileSendFail (MsgItem message,String failMessage) {

        message.setStatus(LWConversationManager.FAIL);
        LWConversationManager.getInstance().updateMsg(message);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MessageAdapter myAdapter = adapter.get();
                if (myAdapter != null){
                    myAdapter.notifyDataSetChanged();
                }
                if (failMessage == null){
                    Toast.makeText(activity,"文件上传异常",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(activity,failMessage,Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

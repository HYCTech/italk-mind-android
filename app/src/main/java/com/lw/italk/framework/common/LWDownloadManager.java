package com.lw.italk.framework.common;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.lw.italk.App;
import com.lw.italk.entity.LWConversation;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.gson.msg.MsgItem;
import com.lw.italk.http.FileDownloadCallback;
import com.lw.italk.http.FileTransferManager;
import com.lw.italk.http.RetrofitCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/9/1 0001.
 */

public class LWDownloadManager {
    private static LWDownloadManager instance = new LWDownloadManager();
    private DownloadManager mDownManager;
    private Executor mExecutor = Executors.newSingleThreadExecutor();
    LWDownloadManager() {
        mDownManager = (DownloadManager) App.getInstance().getSystemService(Context.DOWNLOAD_SERVICE);
    }
    public static synchronized LWDownloadManager getInstance() {
        if (instance == null) {
            instance = new LWDownloadManager();
        }
        return instance;
    }

    public void downloadContactHead(final String url ) {
        Log.e("123qwe", "getLocalimage url:" + url);
        if (TextUtils.isEmpty(url)) {
            return;
        }

        String filename = ".jpg";

        FileTransferManager.getInstance().downLoadFile(filename,url,false,new FileDownloadCallback<ResponseBody>(){

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }

            @Override
            public void onSuccess(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response,String localPath) {
                Log.e("123qwe", "down finish onSuccessCallback url:" + url);
                Log.e("123qwe", "down finish onSuccessCallback result:" + localPath);
                List<Contact> contactList = LWFriendManager.getInstance().queryFriendItemByUrl(url);
//                List<MsgItem> msgItemList = LWConversationManager.getInstance().getMsgByFileName(filename);
                MsgItem msgItem = null;
                for (Contact item : contactList) {
                    item.setLocalimage(localPath);
                    LWFriendManager.getInstance().updateContact(item);
                }
            }

            @Override
            public void onLoading(long total, long progress) {

            }
        });
//        OssManager.getInstance().startDownload(filename, "",  new OssManager.ProgressCallback() {
//            @Override
//            public void onProgressCallback(double progress) {
//                Log.e("123qwe", "progress:" + progress);
//            }
//
//            @Override
//            public void onSuccessCallback(String filename, String path, String result) {
//                Log.e("123qwe", "down finish onSuccessCallback url:" + url);
//                Log.e("123qwe", "down finish onSuccessCallback result:" + result);
//                List<Contact> contactList = LWFriendManager.getInstance().queryFriendItemByUrl(url);
////                List<MsgItem> msgItemList = LWConversationManager.getInstance().getMsgByFileName(filename);
//                MsgItem msgItem = null;
//                for (Contact item : contactList) {
//                    item.setLocalimage(result);
//                    LWFriendManager.getInstance().updateContact(item);
//                }
//            }
//
//            @Override
//            public void onErrorCallback(String filename, String path, String error) {
//
//            }
//        });
    }

    public long startDownload(final String msgID,String fid, final String url) {
        return startDownload(msgID, fid,url, null, false);
    }

    public long startDownload(final String msgID,final String fid, final String url, final OssManager.ProgressCallback callback, final boolean isThumb) {
        Log.e("123qwe", "downloadPath:" + msgID);
        if (/*TextUtils.isEmpty(filename) || */TextUtils.isEmpty(url)) {
            return -1;
        }
        MsgItem msgItem = LWConversationManager.getInstance().getMsgByMsgId(msgID,fid);
        String fileName = msgID;
        if (msgItem != null && msgItem.getFilename() != null){
            fileName = msgItem.getFilename();
        }
        FileTransferManager.getInstance().downLoadFile(fileName,url,isThumb,new FileDownloadCallback<ResponseBody>(){

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }

            @Override
            public void onSuccess(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response,String localPath) {
                Log.e("123qwe", "down finish onSuccessCallback:");
                MsgItem msgItem = LWConversationManager.getInstance().getMsgByMsgId(msgID,fid);
//                MsgItem msgItem = null;
//                for (MsgItem item : msgItemList) {
//                    if (item.getDirect() == LWConversationManager.DIRECT_RECEIVE) {
//                        msgItem = item;
//                        break;
//                    }
//                }
                if (msgItem != null) {
//                    Uri downloadFileUri = mDownManager.getUriForDownloadedFile(myDwonloadID);
//                    if (downloadFileUri != null) {
                    if (isThumb) {
                        msgItem.setLocalthumburl(localPath);
                    } else {
                        msgItem.setLocalpath(localPath);
                    }

                    Log.e("123qwe", "down onSuccessCallback path:" + localPath + ",filename:" + msgID);
                    LWConversationManager.getInstance().updateMsg(msgItem);
                    if (callback != null) {
                        callback.onSuccessCallback(msgID, msgID, localPath);
                    }
                    if (null != LWJNIManager.getInstance().getmMsgUpdateListen()) {
                        List<MsgItem> items = new ArrayList<>();
                        items.add(msgItem);
                        LWJNIManager.getInstance().getmMsgUpdateListen().updateMsgs(items);
                    }
//                    }
                }
            }

            @Override
            public void onLoading(long progress, long total) {
                if(callback != null){
                    callback.onProgressCallback( progress*100/total );
                }
            }
        });

//        OssManager.getInstance().startDownload(objNamee, filename, new OssManager.ProgressCallback() {
//            @Override
//            public void onProgressCallback(double progress) {
//                Log.e("123qwe", "progress:" + progress);
//                if(callback != null){
//                    callback.onProgressCallback(progress);
//                }
//            }
//
//            @Override
//            public void onSuccessCallback(String obj, String file, String result) {
//                Log.e("123qwe", "down finish onSuccessCallback:");
//                List<MsgItem> msgItemList = LWConversationManager.getInstance().getMsgByFileName(file);
//                MsgItem msgItem = null;
//                for (MsgItem item : msgItemList) {
//                    if (item.getDirect() == LWConversationManager.DIRECT_RECEIVE) {
//                        msgItem = item;
//                        break;
//                    }
//                }
//                if (msgItem != null) {
////                    Uri downloadFileUri = mDownManager.getUriForDownloadedFile(myDwonloadID);
////                    if (downloadFileUri != null) {
//                    if (isThumb) {
//                        msgItem.setLocalthumburl(result);
//                    } else {
//                        msgItem.setLocalpath(result);
//                    }
//
//                    Log.e("123qwe", "down onSuccessCallback path:" + result + ",filename:" + filename);
//                        LWConversationManager.getInstance().updateMsg(msgItem);
//                        if (callback != null) {
//                            callback.onSuccessCallback(obj, file, result);
//                        }
//                        if (null != LWJNIManager.getInstance().getmMsgUpdateListen()) {
//                            LWJNIManager.getInstance().getmMsgUpdateListen().updateMsg();
//                        }
////                    }
//                }
//            }
//
//            @Override
//            public void onErrorCallback(String filename, String path, String error) {
//
//            }
//        });

        return 0;
    }
}

package com.lw.italk.http;

import android.content.Context;

import com.lw.italk.http.model.SmsEntityRequest;
import com.lw.italk.utils.CommonUtils;
import com.lw.italk.view.ProgressSubscriber;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by cts on 2017/3/10.
 */
public class HttpUtils {

    public static <E> void doPost(Context context, String path, SmsEntityRequest map, boolean showProgress, Response<E> response) {
        doPost(context, path, map, showProgress, RequestCode.EMPTY, response);
    }

    public static <E> void doPost(Context context, String path, Object data, boolean showProgress, int requestCode, Response<E> response) {
        ThirdLibs.getInstance().getHttpService()
                .post(path, data)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<E>(context, showProgress, requestCode, response));
    }

    public static <E> void doUpload(Context context, String path, Map<String, RequestBody> map,
                                    MultipartBody.Part part, boolean showProgress, int requestCode, Response<E> response) {
        ThirdLibs.getInstance().getHttpService()
                .upload(path, map, part)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<E>(context, showProgress, requestCode, response));
    }

    public static <E> void doPostForm(Context context, String path, SmsEntityRequest map, boolean showProgress, Response<E> response) {

        doPostForm(context, path,map , showProgress, RequestCode.EMPTY, response);

    }

    public static <E> void doPostForm(Context context, String path, Object data, boolean showProgress, int requestCode, Response<E> response) {
        try{
            Map<String, String> map = CommonUtils.ObjectToMapUtil(data);
            ThirdLibs.getInstance().getHttpService()
                    .postForm(path, map)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ProgressSubscriber<E>(context, showProgress, requestCode, response));
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static <E> void doPostFormMap(Context context, String path, Map<String, String> map, boolean showProgress, int requestCode, Response<E> response) {
        try{
            ThirdLibs.getInstance().getHttpService()
                    .postForm(path, map)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ProgressSubscriber<E>(context, showProgress, requestCode, response));
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}

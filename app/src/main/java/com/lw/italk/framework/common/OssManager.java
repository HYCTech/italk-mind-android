package com.lw.italk.framework.common;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.lw.italk.App;
import com.lw.italk.gson.OssAuthentication;
import com.lw.italk.http.ThirdLibs;
import com.lw.italk.utils.ItalkLog;
import com.lw.italk.utils.PathUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Administrator on 2018/8/24 0024.
 */

public class OssManager {
    private static OssManager instance = null;
    private OSS oss;
    private final static String ACCESS_KEY_ID = "LTAIkXDzDpdgw48g";
    private final static String ACCESS_KEY_SECRET = "S2O3ivjLSnIpJDx59P1tjfoezzU6VM";
    private final static String END_POINT = "oss-cn-hangzhou.aliyuncs.com";
    private final static String BUCKET_NAME = "italk8";
    private ExecutorService mExcutorServcie = null;
    private boolean isInit = false;

    private OssManager() {
        mExcutorServcie = Executors.newSingleThreadExecutor();
//        initOSSClient();
    }

    public static synchronized OssManager getInstance() {
        if(instance == null) {
            instance = new OssManager();
        }
        return instance;
    }

    public void initOSSClient(OssAuthentication body) {
        isInit = true;
        Log.e("123qwe", "body.getAccessKeyId():" + body.getAccessKeyId());
        Log.e("123qwe", "body.getAccessKeySecret():" + body.getAccessKeySecret());
        Log.e("123qwe", "body.getSecurityToken():" + body.getSecurityToken());

        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(body.getAccessKeyId(), body.getAccessKeySecret(), body.getSecurityToken());
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(8); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        // oss为全局变量，endpoint是一个OSS区域地址
        oss = new OSSClient(App.getInstance(), END_POINT, credentialProvider, conf);
    }

    public void beginupload(String filename, String path, final ProgressCallback progressCallback) {
        if (path == null || path.equals("")) {
//            LogUtil.d("请选择图片....");
            //ToastUtils.showShort("请选择图片....");
            return;
        }
        final String objectname = getObjectName(filename);
        if (objectname == null || objectname.equals("")) {

            return;
        }
        PutObjectRequest put = new PutObjectRequest(BUCKET_NAME, objectname, path);
        getAuth(objectname, put, progressCallback);
    }

    private void getAuth(final String objectname, final PutObjectRequest put, final ProgressCallback progressCallback) {
        ThirdLibs.getInstance().getOssAuthenticationService().get("sts")
                .enqueue(new Callback<OssAuthentication>() {
                    @Override
                    public void onResponse(Call<OssAuthentication> call, retrofit2.Response<OssAuthentication> response) {
                        try {
                            OssAuthentication body = response.body();
                            initOSSClient(body);
                            beginupload(objectname, put, progressCallback);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ItalkLog.o("IOException:" + e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<OssAuthentication> call, Throwable throwable) {

                    }
                });
    }

    public final String getObjectName(String filename) {
        return "android/" + LWUserManager.getInstance().getUserInfo().getUid()  + filename;
    }

    public void beginupload(String filename, byte[] data, final ProgressCallback progressCallback) {
        if (data == null && data.length == 0){
            return;
        }
        String objectname = getObjectName(filename);
        Log.e("123qwe", "object:" + objectname);
        if (objectname == null || objectname.equals("")) {

            return;
        }
        PutObjectRequest put = new PutObjectRequest(BUCKET_NAME, objectname, data);
        getAuth(objectname, put, progressCallback);
    }

    private void beginupload(final String objectname, PutObjectRequest put, final ProgressCallback progressCallback) {

        //通过填写文件名形成objectname,通过这个名字指定上传和下载的文件
//        String objectname = filename;
//        if (objectname == null || objectname.equals("")) {
//
//            return;
//        }
//        put.setCallbackParam(new HashMap<String, String>() {
//            {
//                put("callbackUrl", path);
//                put("callbackBody", "filename=${object}&size=${size}&id=${x:id}&action=${x:action}");
////https://help.aliyun.com/document_detail/31989.html?spm=5176.doc31984.6.883.brskVg
//            }
//        });

        //下面3个参数依次为bucket名，Object名，上传文件路径
//        PutObjectRequest put = new PutObjectRequest(BUCKET_NAME, objectname, path);

//        LogUtil.d("正在上传中....");
        //ToastUtils.showShort("正在上传中....");
        // 异步上传，可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//                LogUtil.d("currentSize: " + currentSize + " totalSize: " + totalSize);
                double progress = currentSize * 1.0 / totalSize * 100.f;

                if (progressCallback != null) {
                    progressCallback.onProgressCallback(progress);
                }
            }
        });
        @SuppressWarnings("rawtypes")
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
//                LogUtil.d("UploadSuccess");
                //ToastUtils.showShort("上传成功");
                Log.e("123qwe", "result:" + objectname);
                if (progressCallback != null) {
                    progressCallback.onSuccessCallback(request.getObjectKey(), request.getUploadFilePath(), "https://" + BUCKET_NAME + "." + END_POINT + "/" + objectname);
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
//                LogUtil.d("UploadFailure");
//                ToastUtils.showShort("UploadFailure");
                String error = "";
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
//                    LogUtil.e("UploadFailure：表示向OSS发送请求或解析来自OSS的响应时发生错误。\n" +
//                            "  *例如，当网络不可用时，这个异常将被抛出");
                    error = clientExcepion.getMessage();
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    error = serviceException.getMessage();
                    // 服务异常
                    serviceException.printStackTrace();
                }
                if (progressCallback != null) {
                    progressCallback.onErrorCallback(request.getObjectKey(), request.getUploadFilePath(), error);
                }
            }
        });
        //task.cancel(); // 可以取消任务
        //task.waitUntilFinished(); // 可以等待直到任务完成
    }

    public interface ProgressCallback {
        void onProgressCallback(double progress);
        void onSuccessCallback(String objname, String filename, String result);
        void onErrorCallback(String filename, String path, String error);
    }

    public void startDownload(final String objectKey, final String fileName, final ProgressCallback callback) {
        startDownload(objectKey, fileName, callback, false);
    }

    private void download(final String objectKey, final String fileName,final ProgressCallback callback, final boolean isThumb) {
        //构造下载文件请求
        GetObjectRequest get = new GetObjectRequest(BUCKET_NAME, objectKey);
        if (isThumb) {
            get.setxOssProcess("image/resize,m_fixed,w_100,h_100");
        }
        //设置下载进度回调
        get.setProgressListener(new OSSProgressCallback<GetObjectRequest>() {
            @Override
            public void onProgress(GetObjectRequest request, long currentSize, long totalSize) {
                if (callback != null) {
                    callback.onProgressCallback(currentSize / (double) totalSize);
                }
                Log.e("123qwe","getobj_progress: " + currentSize+"  total_size: " + totalSize);
            }
        });
        try {
            // 同步执行下载请求，返回结果
            GetObjectResult getResult = oss.getObject(get);
            Log.d("Content-Length", "" + getResult.getContentLength());
            String path = PathUtil.getInstance().getFilePath();
            String[] tmp = objectKey.split("/");
            String filename = "";
            if (tmp != null && tmp.length > 0) {
                filename = tmp[tmp.length - 1];
            } else {
            }
            String downPath = "";
            if (isThumb) {
                downPath = path + "/local/" + System.currentTimeMillis() + filename;
            } else {
                downPath = path + "/" + System.currentTimeMillis() + "_" + filename;
            }
            File file = new File(downPath);
            Log.e("123qwe", "path:" + path + ",objectKey:" + objectKey + ",filename:" + filename);
            if (!file.exists()){
                file.createNewFile();
            }
            OutputStream outputStream = new FileOutputStream(file);
            // 获取文件输入流
            InputStream inputStream = getResult.getObjectContent();
            byte[] buffer = new byte[2048];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                // 处理下载的数据，比如图片展示或者写入文件等
                outputStream.write(buffer, 0, len);
            }
            inputStream.close();
            outputStream.close();
            // 下载后可以查看文件元信息
            ObjectMetadata metadata = getResult.getMetadata();
            if (callback != null) {
                callback.onSuccessCallback(objectKey, fileName, file.getPath());
            }
            Log.d("ContentType", metadata.getContentType());
        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();
            if (callback != null) {
                callback.onErrorCallback(objectKey, fileName, e.getMessage());
            }
        } catch (ServiceException e) {
            if (e == null) {
                if (callback != null) {
                    callback.onErrorCallback(objectKey, fileName, "");
                }
                return;
            }
            e.printStackTrace();
            if (callback != null) {
                callback.onErrorCallback("", "", e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onErrorCallback("", "", e.getMessage());
            }
        }
    }

    public void startDownload(final String objectKey, final String fileName,final ProgressCallback callback, final boolean isThumb) {
        if (isInit) {
            try {
                mExcutorServcie.execute(new Runnable() {
                    @Override
                    public void run() {
                        download(objectKey, fileName, callback, isThumb);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                ItalkLog.o("IOException:" + e.getMessage());
            }
        }
        ThirdLibs.getInstance().getOssAuthenticationService().get("sts")
                .enqueue(new Callback<OssAuthentication>() {
                    @Override
                    public void onResponse(Call<OssAuthentication> call, retrofit2.Response<OssAuthentication> response) {
                        try {
                            final OssAuthentication body = response.body();
                            mExcutorServcie.execute(new Runnable() {
                                @Override
                                public void run() {
                                    initOSSClient(body);
                                    download(objectKey, fileName, callback, isThumb);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            ItalkLog.o("IOException:" + e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<OssAuthentication> call, Throwable throwable) {

                    }
                });


    }
}

package com.lw.italk.http;

import com.italkmind.client.util.ClientTools;
import com.jakewharton.retrofit2.adapter.rxjava2.Result;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.lw.italk.GloableParams;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.utils.CommonUtils;
import com.lw.italk.utils.FileUtils;
import com.lw.italk.utils.ItalkLog;
import com.yixia.camera.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FileTransferManager {
    private String TAG = "FileTransferManager";
    private static FileTransferManager sInstance;
    private FileService mFileService;
    private static final long MAX_CONNECT_TIME = 10;
    private FileTransferManager() {


        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new FileInterceptor())
                .connectTimeout(MAX_CONNECT_TIME, TimeUnit.SECONDS)
                .readTimeout(MAX_CONNECT_TIME, TimeUnit.SECONDS)
//                .hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        //强行返回true 即验证成功
//                        return true;
//                    }
//                })
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.FILE_DOMAIN)
                .client(client)
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mFileService = retrofit.create(FileService.class);

    }


    public static FileTransferManager getInstance() {
        if (sInstance == null) {
            synchronized (ThirdLibs.class) {
                if (sInstance == null) {
                    return new FileTransferManager();
                }
            }
        }
        return sInstance;
    }


    public void uploadFile(File file,int recType,long recId, int fileType, RetrofitCallback<ResponseBody> callback){

        RequestBody resquestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        //通过该行代码将RequestBody转换成特定的FileRequestBody
        FileRequestBody body = new FileRequestBody(resquestBody, callback);
        MultipartBody.Part bodyPart = MultipartBody.Part.createFormData("file", file.getName(), body);
        Call<ResponseBody> call = mFileService.uploadFile(bodyPart,recType,recId,fileType);
        call.enqueue(callback);
    }

    public void downLoadFile(String fileName ,String downloadUrl,boolean isThumb,FileDownloadCallback<ResponseBody> callback){

        if (downloadUrl.contains(AppConfig.FILE_DOMAIN)){
            downloadUrl.replace(AppConfig.FILE_DOMAIN,"");
        }else{
//            return;
        }
        Call<ResponseBody> responseBodyCall = mFileService.downloadFile(downloadUrl);
//        responseBodyCall.enqueue(callback);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final retrofit2.Response<ResponseBody> response) {

//                L.d("vivi",response.message()+"  length  "+response.body().contentLength()+"  type "+response.body().contentType());
                //建立一个文件
                final File  file = FileUtils.createFile(fileName,isThumb);
                if(!response.isSuccessful()){
                    callback.onFailure(call,new Throwable(response.message()));
                }
                //下载文件放在子线程
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        //保存到本地
                        FileUtils.writeFile2Disk(response, file, callback);
                    }
                }.start();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(call,t);
            }
        });
    }

    private static final class FileInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            builder.addHeader("appVersion", GloableParams.versionName);
            builder.addHeader("uniqueId", CommonUtils.getInstallIMEI());
            if (LWUserManager.getInstance().getUserInfo() != null) {
                builder.addHeader("IM-Uid", LWUserManager.getInstance().getUserInfo().getUid());

                String timestamp = System.currentTimeMillis() / 1000 + "";
                builder.addHeader("IM-timestamp", timestamp);
                String path = "/chitchat/uploadFile";
                if (request.url().toString().contains("/chitchat/file/")) {
                    path = "/chitchat/file/";
                }

                String md5String = LWUserManager.getInstance().getUserInfo().getUid() + "&" + timestamp + "&" +
                        path + "&" + LWUserManager.getInstance().getFileAuthid();
                builder.addHeader("IM-Auth", ClientTools.hashMd5(md5String));
            }else {
                Log.e("FileInterceptor","GloableParams.CurUser is null");
            }

            Request newquest =builder.build();
            ItalkLog.i(request.url().toString());
//            ItalkLog.o("request-body:" + request.body().toString());
            return chain.proceed(newquest);
        }
    }
}

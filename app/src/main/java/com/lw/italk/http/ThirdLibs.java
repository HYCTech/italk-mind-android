package com.lw.italk.http;

import android.util.Log;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.lw.italk.GloableParams;
import com.lw.italk.utils.CommonUtils;
import com.lw.italk.utils.FileUtils;
import com.lw.italk.utils.ItalkLog;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cts on 2016/12/7.
 */
public class ThirdLibs {

    private final static String TAG = ThirdLibs.class.getSimpleName();
    private static final long MAX_CONNECT_TIME = 10;
    private static final String VERSION_NAME = "versionName";
    private static ThirdLibs sInstance;
    private HttpService mHttpService;
    private Gson mGson;
    private OkHttpClient client;

    private ThirdLibs() {

        mGson = new Gson();

        CommonInterceptor interceptor =  new CommonInterceptor();

        client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(MAX_CONNECT_TIME, TimeUnit.SECONDS)
                .addNetworkInterceptor(getNetWorkInterceptor())
                .readTimeout(MAX_CONNECT_TIME, TimeUnit.SECONDS)
                .writeTimeout(MAX_CONNECT_TIME, TimeUnit.SECONDS)
//                .hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        //强行返回true 即验证成功
//                        return true;
//                    }
//                })
                .build();

        mHttpService = new Retrofit.Builder()
                .baseUrl(AppConfig.DOMAIN)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(HttpService.class);


    }

    private ThirdLibs(String get) {

        mGson = new Gson();

        CommonInterceptor interceptor =  new CommonInterceptor();

        client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(MAX_CONNECT_TIME, TimeUnit.SECONDS)
                .readTimeout(MAX_CONNECT_TIME, TimeUnit.SECONDS)
                .writeTimeout(MAX_CONNECT_TIME, TimeUnit.SECONDS)
                .build();

        mHttpService = new Retrofit.Builder()
                .baseUrl(AppConfig.DOMAIN)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(HttpService.class);


    }

    /**
     * 描述:返回网络拦截器 打印请求参数和返回结果
     */
    private Interceptor getNetWorkInterceptor() {
        Interceptor authorizationInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException
            {

                Request request = chain.request();

                long startTime = System.currentTimeMillis();
                okhttp3.Response response = chain.proceed(chain.request());
                long endTime = System.currentTimeMillis();
                long duration=endTime-startTime;
                okhttp3.MediaType mediaType = response.body().contentType();
                String content = response.body().string();
                Log.d(TAG,"\n");
                Log.d(TAG,"-------------------------------Start-------------------------------------");
                Log.d(TAG, "| "+request.toString());
                String method=request.method();
                Log.d(TAG, method);
                if("POST".equals(method)){
                    StringBuilder sb = new StringBuilder();
                    if (request.body() instanceof FormBody) {
                        FormBody body = (FormBody) request.body();
                        for (int i = 0; i < body.size(); i++) {
                            sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                        }
                        sb.delete(sb.length() - 1, sb.length());
                        Log.d(TAG, "| RequestParams:{"+sb.toString()+"}");
                    }
                }
                Log.d(TAG, "| Response:" + content);
                Log.d(TAG,"------------------------End:"+duration+"毫秒--------------------------------------");
                return response.newBuilder()
                        .body(okhttp3.ResponseBody.create(mediaType, content))
                        .build();

            }
        };
        return authorizationInterceptor;
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }



    public static ThirdLibs getInstance() {
        if (sInstance == null) {
            synchronized (ThirdLibs.class) {
                if (sInstance == null) {
                    return new ThirdLibs();
                }
            }
        }
        return sInstance;
    }

    public static ThirdLibs getInstanceGet() {
        if (sInstance == null) {
            synchronized (ThirdLibs.class) {
                if (sInstance == null) {
                    return new ThirdLibs("get");
                }
            }
        }
        return sInstance;
    }

    public HttpService getHttpService() {
        return mHttpService;
    }

    public HttpService getOssAuthenticationService() {
        HttpService mOssAuthentication = new Retrofit.Builder()
                .baseUrl(AppConfig.OSS_AUTHENTICATION)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(HttpService.class);
        return mOssAuthentication;
    }

    public void downLoadFile(String fileName ,String downloadUrl,boolean isThumb,FileDownloadCallback<ResponseBody> callback){


        Call<ResponseBody> responseBodyCall = mHttpService.getFile(downloadUrl);
//        responseBodyCall.enqueue(callback);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final retrofit2.Response<ResponseBody> response) {

//                L.d("vivi",response.message()+"  length  "+response.body().contentLength()+"  type "+response.body().contentType());
                System.out.println("-PathUitl---fileName -->"+fileName+"--isThumb----"+isThumb);
                if(fileName!=null){
                    //建立一个文件
                    final File file = FileUtils.createFile(fileName,isThumb);
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

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(call,t);
            }
        });
    }

    public void uploadAvatar(File file,String tokenId, RetrofitCallback<ResponseBody> callback){

        RequestBody resquestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        //通过该行代码将RequestBody转换成特定的FileRequestBody
        FileRequestBody body = new FileRequestBody(resquestBody, callback);
        MultipartBody.Part bodyPart = MultipartBody.Part.createFormData("avatar", file.getName(), body);

        RequestBody description =
                RequestBody.create(MediaType.parse("text/plain"), tokenId);
        Map<String, RequestBody> bodyMap = new HashMap<>();
        bodyMap.put("tokenId", description);
//        bodyMap.put("photo\"; filename=\""+file.getName(), RequestBody.create(MediaType.parse("image/jpg"),file));

        Call<ResponseBody> call = mHttpService.uploadAvatar(bodyPart,bodyMap);
        call.enqueue(callback);
    }

    public Gson getGson() {
        return mGson;
    }

    private static final class CommonInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            builder.addHeader("appVersion", /*"1.0.0"*/GloableParams.versionName);
            builder.addHeader("uniqueId", CommonUtils.getInstallIMEI());
//            if (request.url().toString().contains("/user/editAvatar")) {
//                builder.addHeader("tokenId", GloableParams.CurUser.getToken());
//            }
//            builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
//            if (!AccountManager.INSTANCE.isLogin()) {
//                builder.addHeader("authorization", AccountManager.INSTANCE.getAccount().getToken());
//            }
//            RequestBody requestBody = new FormBody.Builder().add("account", "18695732196")
//                    .add("password", ClientTools.hashMd5("abc123")).build();
//            Request request = new Request.Builder().url(url).post(requestBody).build();

//            Request newquest =builder.post(requestBody).build();
            ItalkLog.i(request.url().toString());
//            ItalkLog.o("request-body:" + request.body().toString());
            return chain.proceed(builder.build());
        }
    }

}

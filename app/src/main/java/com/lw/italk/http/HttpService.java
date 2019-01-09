package com.lw.italk.http;

import com.lw.italk.gson.OssAuthentication;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by cts on 2016/12/8.
 */
public interface HttpService {
    @FormUrlEncoded
    @POST("{path}")
    Flowable<ResponseBody> postForm(@Path(value = "path", encoded = true) String path, @FieldMap Map<String,String> map);

    @POST("{path}")
    Flowable<ResponseBody> post(@Path(value = "path", encoded = true) String path, @Body Object map);

    @GET("{path}")
    Call<OssAuthentication> get(@Path(value = "path", encoded = true) String path);

    @POST("{path}")
    Flowable<ResponseBody> upload(@Path(value = "path", encoded = true) String path, @PartMap Map<String, RequestBody> map, @Part MultipartBody.Part part);


    @Streaming
    @GET
    Call<ResponseBody> getFile(@Url String url);

    @Multipart
    @POST("/user/editAvatar")
    Call<ResponseBody> uploadAvatar(@Part MultipartBody.Part file,@PartMap Map<String, RequestBody> params);

}

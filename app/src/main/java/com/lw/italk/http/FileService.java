package com.lw.italk.http;

import com.jakewharton.retrofit2.adapter.rxjava2.Result;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface FileService {

//    @FormUrlEncoded
    @Multipart
    @POST("/chitchat/uploadFile")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part file,@Part("recType") int recType,@Part("recId") long recId,@Part("type") int type);

//    @FormUrlEncoded
//    @POST("/chitchat/uploadFile")
    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String url);
}

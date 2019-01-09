package com.lw.italk.http;


import retrofit2.Call;
import retrofit2.Callback;

public abstract class RetrofitCallback<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, retrofit2.Response<T> response) {
        if(response.isSuccessful()) {
            onSuccess(call, response);
        } else {
            onFailure(call, new Throwable(response.message()));
        }
    }
    public abstract void onSuccess(Call<T> call, retrofit2.Response<T> response);
    //用于进度的回调
    public abstract void onLoading(long total, long progress) ;

}

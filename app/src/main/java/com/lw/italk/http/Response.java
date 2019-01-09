package com.lw.italk.http;

import java.lang.reflect.Type;

/**
 * Created by cts on 2017/3/10.
 */
public interface Response<T> {
    void next(T t, int requestCode);

    void error(ResponseErrorException t, int requestCode);

    Type getTypeToken(int requestCode);
}


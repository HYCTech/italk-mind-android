package com.lw.italk.http;

import java.util.HashMap;

/**
 * Created by cts on 2017/3/13.
 */
public class EmptyExtraParams extends RequestParams {

    private EmptyExtraParams() {
        super();
    }

    public static RequestParams newInstance() {
        return new EmptyExtraParams();
    }

    @Override
    public void addExtraParams(HashMap<String, String> map) {

    }
}

package com.stog.android.api.response;

public class Response<T> {
    boolean success;
    String info;
    T data;

    public boolean isSuccess() {
        return success;
    }
    public String getInfo() {
        return info;
    }
    public T getData() {
        return data;
    }
}

package com.stog.android.api;

public abstract class ResponseCallback<T> {
    abstract public void onResponse(T response);
    abstract public void onError(String error);
}
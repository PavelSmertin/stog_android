package com.stog.android.api;

import android.content.Context;
import android.os.Looper;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.stog.android.api.response.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class RestClient {

    private static final RestClient INSTANCE = new RestClient();

    private static final String BASE_URL = "http://secret-atoll-68402.herokuapp.com/api/v1/";

    private static final String HEADER_AUTH_LOGIN = "X-User-Phone";
    private static final String HEADER_AUTH_TOKEN = "X-User-Token";

    private List<OnLogoutListener> logoutListeners = new ArrayList<>();

    private AsyncHttpClient client = new AsyncHttpClient();
    public AsyncHttpClient syncHttpClient= new SyncHttpClient();


    public static RestClient getInstance() {
        return INSTANCE;
    }

    private RestClient() {

    }


    public void setHeaderLogin(String login){
        client.addHeader(HEADER_AUTH_LOGIN, login);
        client.removeHeader(HEADER_AUTH_TOKEN);
    }

    public void setHeaders(String login, String token){
        client.addHeader(HEADER_AUTH_LOGIN, login);
        client.addHeader(HEADER_AUTH_TOKEN, token);
    }

    /**
     * @return an async client when calling from the main thread, otherwise a sync client.
     */
    private AsyncHttpClient getClient() {
        AsyncHttpClient cl = client;
        // Return the synchronous HTTP client when the thread is not prepared
        if (Looper.myLooper() == null) {
            cl = syncHttpClient;
        }
        return cl;
    }

    public void addOnLogoutListener(OnLogoutListener onLogoutListener) {
        if (logoutListeners.contains(onLogoutListener)) {
            return;
        }
        logoutListeners.add(onLogoutListener);
    }

    public void removeOnLogoutListener(OnLogoutListener onLogoutListener) {
        if (!logoutListeners.contains(onLogoutListener)) {
            return;
        }
        logoutListeners.remove(onLogoutListener);
    }

    private void notifyOnLogoutListeners() {
        for (OnLogoutListener listener : logoutListeners) {
            listener.onLogout();
        }
    }

    public <T> void get(String method, InitialRequestParams params, Type type, ResponseCallback<T> responseHandler) {
        getClient().get(getAbsoluteUrl(method), params, getCallback(type, responseHandler));
    }

    public <T> void post(String method, InitialRequestParams params, Type type, ResponseCallback<T> responseHandler) {
        getClient().post(getAbsoluteUrl(method), params, getCallback(type, responseHandler));
    }

    public <T> void postRaw(Context context, String method, InitialRequestParams params, StringEntity entity, Type type, ResponseCallback<T> responseHandler) {
        getClient().post(context, getAbsoluteUrl(method), entity, "application/json",  getCallback(type, responseHandler));
    }


    public <T> void put(String method, InitialRequestParams params, Type type, ResponseCallback<T> responseHandler) {
        getClient().put(getAbsoluteUrl(method), params, getCallback(type, responseHandler));
    }

    public <T> void delete(String method, InitialRequestParams params, Type type, ResponseCallback<T> responseHandler) {
        getClient().delete(getAbsoluteUrl(method), params, getCallback(type, responseHandler));
    }

    private <T> JsonHttpResponseHandler getCallback(final Type type, final ResponseCallback<T> methodCallback){
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = new Gson();
                Response<T> resp = gson.fromJson(response.toString(), type);
                methodCallback.onResponse(resp.getData());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                String exp = "code: " + statusCode + " throwable: " + (throwable != null ? throwable.getLocalizedMessage() : "null") + " errorResponse: " + (errorResponse != null ? errorResponse.toString() : "null");
                //ErrorHelper.errorHandle(new Exception(exp));
                methodCallback.onError(exp);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                String exp = "code: " + statusCode + " throwable: " + (throwable != null ? throwable.getLocalizedMessage() : "null") + " errorResponse: " + (errorResponse != null ? errorResponse.toString() : "null");
                //ErrorHelper.errorHandle(new Exception(exp));
                methodCallback.onError(exp);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                String exp = "code: " + statusCode + " throwable: " + (throwable != null ? throwable.getLocalizedMessage() : "null") + " responseString: " + responseString;
                //ErrorHelper.errorHandle(new Exception(exp));
                methodCallback.onError(exp);
            }
        };
    }

    private String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public interface OnLogoutListener {
        void onLogout();
    }

}
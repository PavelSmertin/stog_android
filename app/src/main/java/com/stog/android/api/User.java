package com.stog.android.api;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.stog.android.api.response.AuthResponse;
import com.stog.android.api.response.CheckLoginResponse;
import com.stog.android.api.response.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import cz.msebera.android.httpclient.entity.StringEntity;

public class User {
    private int id;
    private String phone;

    /*SINGLETON*/
    public static User getInstance() {
        return SingletonHelper.INSTANCE;
    }
    private User(){}
    private static class SingletonHelper{
        private static final User INSTANCE = new User();
    }

    /*SETTERS GETTERS*/
    public int getId() {
        return id;
    }


    /* API */
    public static void signup(Context context, String phone, String pin, final ResponseCallback<AuthResponse> responseHandler){
        InitialRequestParams params = new InitialRequestParams();

        JSONObject jsonUserParams = new JSONObject();
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("phone", phone);
            jsonParams.put("password", pin);
            jsonUserParams.put("user", jsonParams);
        } catch (JSONException ecxeption) {

        }

        try {
            StringEntity entity = new StringEntity(jsonUserParams.toString());
            Type type = new TypeToken<Response<AuthResponse>>() {}.getType();

            RestClient.getInstance().postRaw(context, "register", params, entity, type, responseHandler);
        } catch (UnsupportedEncodingException exception) {

        }

    }

    public static void signin(Context context, String phone, String pin, final ResponseCallback<AuthResponse> responseHandler){
        InitialRequestParams params = new InitialRequestParams();

        JSONObject jsonUserParams = new JSONObject();
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("phone", phone);
            jsonParams.put("password", pin);
            jsonUserParams.put("user", jsonParams);
        } catch (JSONException ecxeption) {

        }

        try {
            StringEntity entity = new StringEntity(jsonUserParams.toString());
            Type type = new TypeToken<Response<AuthResponse>>() {}.getType();

            RestClient.getInstance().postRaw(context, "sessions", params, entity, type, responseHandler);
        } catch (UnsupportedEncodingException exception) {

        }
    }

    public static void signout(final ResponseCallback responseHandler){
        InitialRequestParams params = new InitialRequestParams();
        Type type = new TypeToken<Response>() {}.getType();
        RestClient.getInstance().delete("sessions", params, type, responseHandler);
    }

    public static void remove(final ResponseCallback responseHandler){
        InitialRequestParams params = new InitialRequestParams();
        Type type = new TypeToken<Response>() {}.getType();
        RestClient.getInstance().delete("register", params, type, responseHandler);
    }

    public static void changePassword(final ResponseCallback responseHandler){
        InitialRequestParams params = new InitialRequestParams();
        Type type = new TypeToken<Response>() {}.getType();
        RestClient.getInstance().delete("user_change_password", params, type, responseHandler);
    }

    public static void checkLogin(String phone, final ResponseCallback<CheckLoginResponse> responseHandler) {
        InitialRequestParams params = new InitialRequestParams();
        Type type = new TypeToken<Response<CheckLoginResponse>>() {}.getType();
        RestClient.getInstance().setHeaderLogin(phone);
        RestClient.getInstance().get("user_check_exist", params, type, responseHandler);
    }

    public static void uuidConnect(String uuid, final ResponseCallback responseHandler) {
        InitialRequestParams params = new InitialRequestParams();
        params.add("token", uuid);
        Type type = new TypeToken<Response>() {}.getType();
        RestClient.getInstance().post("uuid_connect", params, type, responseHandler);
    }



}

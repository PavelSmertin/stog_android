package com.stog.android.api;

import com.google.gson.reflect.TypeToken;
import com.stog.android.api.response.AuthResponse;
import com.stog.android.api.response.CheckLoginResponse;
import com.stog.android.api.response.Response;
import com.stog.android.storage.PreferencesHelper;

import java.lang.reflect.Type;

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
    public static void signup(String phone, String pin, final ResponseCallback<AuthResponse> responseHandler){
        InitialRequestParams params = new InitialRequestParams();
        params.add("phone", phone);
        params.add("pin", pin);
        Type type = new TypeToken<Response<AuthResponse>>() {}.getType();
        RestClient.post("signup", params, type, responseHandler);
    }

    public static void signin(String phone, String pin, final ResponseCallback<AuthResponse> responseHandler){
        InitialRequestParams params = new InitialRequestParams();
        params.add("phone", phone);
        params.add("pin", pin);
        Type type = new TypeToken<Response<AuthResponse>>() {}.getType();
        RestClient.post("signin", params, type, responseHandler);
    }

    public static void signout(final ResponseCallback responseHandler){
        InitialRequestParams params = new InitialRequestParams();
        Type type = new TypeToken<Response>() {}.getType();
        RestClient.delete("signout", params, type, responseHandler);
    }

    public static void remove(final ResponseCallback responseHandler){
        InitialRequestParams params = new InitialRequestParams();
        Type type = new TypeToken<Response>() {}.getType();
        RestClient.delete("user_remove", params, type, responseHandler);
    }

    public static void changePassword(final ResponseCallback responseHandler){
        InitialRequestParams params = new InitialRequestParams();
        Type type = new TypeToken<Response>() {}.getType();
        RestClient.delete("user_change_password", params, type, responseHandler);
    }

    public static void checkLogin(String phone, final ResponseCallback<CheckLoginResponse> responseHandler) {
        InitialRequestParams params = new InitialRequestParams();
        params.add("phone", phone);
        params.add("email", "");
        Type type = new TypeToken<Response<CheckLoginResponse>>() {}.getType();
        RestClient.post("check_user_exist", params, type, responseHandler);
    }

    public static void uuidConnect(String uuid, final ResponseCallback responseHandler) {
        InitialRequestParams params = new InitialRequestParams();
        params.add("token", uuid);
        params.add("user", PreferencesHelper.getAuthToken());
        Type type = new TypeToken<Response>() {}.getType();
        RestClient.post("MobileUUIDConnectMobile", params, type, responseHandler);
    }



}

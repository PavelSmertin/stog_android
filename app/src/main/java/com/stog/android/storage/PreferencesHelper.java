package com.stog.android.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.stog.android.auth.LoginActivity;

public class PreferencesHelper {

    public static final String NAME_APP = "stog";
    public static final String KEY_GCM_TOKEN = "gcmToken";
    public static final String KEY_AUTH_TOKEN = "authToken";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_AUTH_STATE = "authState";

    private static SharedPreferences sharedPreferences;

    public static void setSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(NAME_APP, Context.MODE_PRIVATE);
    }

    public static void setGcmToken(String token) {
        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_GCM_TOKEN, token);
        editor.apply();
    }

    public static String getGcmToken() {
        return sharedPreferences.getString(KEY_GCM_TOKEN, null);
    }

    public static void setAuthToken(String token) {
        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.apply();
    }

    public static String getUserId() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    public static void setUserId(String token) {
        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.apply();
    }

    public static String getAuthToken() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    public static boolean isAuth() {
        return getAuthToken() != null;
    }

    public static void setLogin(String login) {
        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LOGIN, login);
        editor.apply();
    }

    public static String getLogin() {
        return sharedPreferences.getString(KEY_LOGIN, null);
    }


    public static void setAuthState(LoginActivity.AuthState state) {
        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AUTH_STATE, state.toString());
        editor.apply();
    }

    public static LoginActivity.AuthState getAuthState() {
        String value = sharedPreferences.getString(KEY_AUTH_STATE, null);
        if (value != null) {
            return LoginActivity.AuthState.valueOf(value);
        } else {
            return LoginActivity.AuthState.NONE;
        }
    }

    public static void clearChangePinState() {
        if (getAuthState() == LoginActivity.AuthState.CHANGE_PIN) {
            setAuthState(LoginActivity.AuthState.SIGNIN);
        }
    }

    public static void signout() {
        Editor editor = sharedPreferences.edit();
        editor.remove(KEY_AUTH_TOKEN);
        editor.remove(KEY_AUTH_STATE);
        editor.apply();
    }

}

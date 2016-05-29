package com.stog.android.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.stog.android.auth.LoginActivity;

public class PreferencesHelper {

    private static final PreferencesHelper INSTANCE = new PreferencesHelper();

    public static final String NAME_APP = "stog";
    public static final String KEY_GCM_TOKEN = "gcmToken";
    public static final String KEY_AUTH_TOKEN = "authToken";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_AUTH_STATE = "authState";

    private SharedPreferences sharedPreferences;


    public static PreferencesHelper getInstance() {
        return INSTANCE;
    }

    private PreferencesHelper() {

    }

    public void setSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(NAME_APP, Context.MODE_PRIVATE);
    }

    public void setGcmToken(String token) {
        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_GCM_TOKEN, token);
        editor.apply();
    }

    public String getGcmToken() {
        return sharedPreferences.getString(KEY_GCM_TOKEN, null);
    }

    public void setAuthToken(String token) {
        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.apply();
    }

    public String getAuthToken() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    public boolean isAuth() {
        return getAuthToken() != null;
    }

    public void setLogin(String login) {
        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LOGIN, login);
        editor.apply();
    }

    public String getLogin() {
        return sharedPreferences.getString(KEY_LOGIN, null);
    }

    public void setAuthState(LoginActivity.AuthState state) {
        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AUTH_STATE, state.toString());
        editor.apply();
    }

    public LoginActivity.AuthState getAuthState() {
        String value = sharedPreferences.getString(KEY_AUTH_STATE, null);
        if (value != null) {
            return LoginActivity.AuthState.valueOf(value);
        } else {
            return LoginActivity.AuthState.NONE;
        }
    }

    public void clearChangePinState() {
        if (getAuthState() == LoginActivity.AuthState.CHANGE_PIN) {
            setAuthState(LoginActivity.AuthState.SIGNIN);
        }
    }

    public void signout() {
        Editor editor = sharedPreferences.edit();
        editor.remove(KEY_AUTH_TOKEN);
        editor.remove(KEY_AUTH_STATE);
        editor.apply();
    }

}

package com.stog.android.api.response;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("auth_token")
    String token;

    public String getToken() {
        return token;
    }
}

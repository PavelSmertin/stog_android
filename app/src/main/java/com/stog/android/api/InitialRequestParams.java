package com.stog.android.api;

import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class InitialRequestParams extends RequestParams {

    protected List<BasicNameValuePair> getParamsList() {
        List<BasicNameValuePair> lparams = super.getParamsList();
        lparams.add(new BasicNameValuePair("api_version", "1.0"));
        return lparams;
    }
}

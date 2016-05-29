package com.stog.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stog.android.api.RestClient;
import com.stog.android.auth.LoginActivity;
import com.stog.android.storage.PreferencesHelper;


abstract public class BaseActivity extends AppCompatActivity implements RestClient.OnLogoutListener {

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RestClient.getInstance().addOnLogoutListener(this);
    }

    public void showLoader(String message){
        if(!isFinishing())
        {
            mDialog = new ProgressDialog(this);
            if(message.length() == 0) {
                mDialog.setMessage(getString(R.string.common_please_wait));
            } else {
                mDialog.setMessage(message);
            }
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setCancelable(false);
            mDialog.show();
        }
    }

    public void hideLoader() {
        if(mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    public void onLogout() {
        PreferencesHelper.getInstance().signout();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
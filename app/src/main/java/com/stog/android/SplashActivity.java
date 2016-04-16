package com.stog.android;

import android.content.Intent;
import android.os.Bundle;

import com.stog.android.auth.LoginActivity;
import com.stog.android.storage.PreferencesHelper;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
        worker.schedule(new Runnable() {
            @Override
            public void run() {
                if (PreferencesHelper.isAuth()) {
                    openMain();
                } else {
                    openLogin();
                }
            }
        }, 2, TimeUnit.SECONDS);
    }

    private void openLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void openMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

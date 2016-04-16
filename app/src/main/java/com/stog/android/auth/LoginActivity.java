package com.stog.android.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.stog.android.BaseActivity;
import com.stog.android.MainActivity;
import com.stog.android.R;
import com.stog.android.api.response.AuthResponse;
import com.stog.android.service.GcmConnectService;
import com.stog.android.statistic.StatisticAdapter;
import com.stog.android.storage.PreferencesHelper;

public class LoginActivity extends BaseActivity implements
        LoginPhoneFragment.OnPhoneFragmentInteractionListener,
        LoginPasswordFragment.LoginPasswordFragmentInteractionListener {

    public enum AuthState {
        NONE,
        SIGNUP,
        SIGNIN,
        CHANGE_PIN
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentManager fm = getSupportFragmentManager();
        LoginPhoneFragment fragment = new LoginPhoneFragment();
        fm.beginTransaction().
                replace(R.id.container, fragment, LoginPhoneFragment.class.getName()).
                commit();

        if (PreferencesHelper.isAuth()) {
            next();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        super.onBackPressed();
    }

    void next() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPhoneSendStart() {
        showLoader("");
    }

    @Override
    public void onPhoneSendEnd(AuthState state) {
        hideLoader();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment;
        LoginPasswordFragment f = new LoginPasswordFragment();
        String login = PreferencesHelper.getLogin();
        f.setLogin(login);
        fragment = f;
        fm.beginTransaction().
                setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.back_slide_in, R.anim.back_slide_out).
                replace(R.id.container, fragment, fragment.getClass().getName()).
                addToBackStack(null).
                commitAllowingStateLoss();
    }

    @Override
    public void onPhoneSendError(String error) {
        hideLoader();
    }

    @Override
    public void onPasswordSendStart() {
        showLoader("");
    }

    @Override
    public void onPasswordSendEnd(AuthResponse response) {
        StatisticAdapter.setUser();
        PreferencesHelper.setAuthToken(response.getToken());

        Intent intentGcm = new Intent(LoginActivity.this, GcmConnectService.class);
        startService(intentGcm);

        //hideLoader();
        next();
    }

    @Override
    public void onPasswordSendError(String error) {
        hideLoader();
    }

    @Override
    public void onPasswordChange() {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
    }

}

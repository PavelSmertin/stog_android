package com.stog.android.auth;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.stog.android.R;
import com.stog.android.api.ResponseCallback;
import com.stog.android.api.User;
import com.stog.android.api.response.CheckLoginResponse;
import com.stog.android.statistic.ButtonParamsPull;
import com.stog.android.statistic.StatisticAdapter;
import com.stog.android.storage.PreferencesHelper;
import com.stog.android.view.EditText;

public class LoginPhoneFragment extends Fragment implements TextWatcher{

    private EditText mPhoneEditText;
    private Button mLoginButton;
    private View view;


    private String mLogin;
    private String storagePhoneNumber;

    private OnPhoneFragmentInteractionListener mListener;

    @Nullable
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.fragment_login_phone, null);

        mPhoneEditText = (EditText)view.findViewById(R.id.login_phone);
        //mPhoneEditText.setText("+7");
        //mPhoneEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        mPhoneEditText.addTextChangedListener(this);

        mLoginButton = (Button)view.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticAdapter.sendButtonEvent(new ButtonParamsPull("b_Phone_Ok"));
                loginClick();
            }
        });

        String phoneNumber = PreferencesHelper.getInstance().getLogin();
        storagePhoneNumber = phoneNumber;

        return view;
    }

    private void loginClick() {
        if (mListener != null) {
            mListener.onPhoneSendStart();
        }
        String phoneValue = mPhoneEditText.getText().toString();
        if(phoneValue.contains("*")){
            mLogin = storagePhoneNumber;
        } else {
            mLogin = phoneValue;
        }
        PreferencesHelper.getInstance().setLogin(mLogin);
        checkLogin();
    }

    private void checkLogin() {
        User.checkLogin(mLogin, new ResponseCallback<CheckLoginResponse>() {
            @Override
            public void onResponse(CheckLoginResponse response) {
                LoginActivity.AuthState state;
                if (response.getId() > 0 ) {
                    //StatisticAdapter.sendButtonEvent(new ButtonParamsPull("e_user_exist"));
                    state = LoginActivity.AuthState.SIGNIN;
                } else {
                    //StatisticAdapter.sendButtonEvent(new ButtonParamsPull("e_user_new"));
                    state = LoginActivity.AuthState.SIGNUP;
                }

                if (mListener != null) {
                    mListener.onPhoneSendEnd(state);
                }
            }

            @Override
            public void onError(String error) {
                if (mListener != null) {
                    mListener.onPhoneSendError(error);
                }
            }
        });
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            if (mListener == null) {
                mListener = (OnPhoneFragmentInteractionListener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPhoneFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(mPhoneEditText.isValid()) {
            mLoginButton.setEnabled(true);
        } else {
            mLoginButton.setEnabled(false);
        }
    }

    public interface OnPhoneFragmentInteractionListener {
        void onPhoneSendStart();
        void onPhoneSendEnd(LoginActivity.AuthState state);
        void onPhoneSendError(String error);
    }

}

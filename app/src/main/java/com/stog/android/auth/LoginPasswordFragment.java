package com.stog.android.auth;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stog.android.R;
import com.stog.android.api.ResponseCallback;
import com.stog.android.api.User;
import com.stog.android.api.response.AuthResponse;
import com.stog.android.statistic.ButtonParamsPull;
import com.stog.android.statistic.StatisticAdapter;
import com.stog.android.storage.PreferencesHelper;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class LoginPasswordFragment extends Fragment {

    private View view;

    private EditText mPinCodeView;
    private TextView mLoginPasswordHint;
    private Button mChangePasswordButton;
    private Button mLoginPasswordButton;

    private LoginPasswordFragmentInteractionListener mListener;
    private String pinCodeFirst;


    @Nullable
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.fragment_login_password, null);

        mPinCodeView = (EditText)view.findViewById(R.id.login_password);

        mLoginPasswordButton = (Button)view.findViewById(R.id.login_pass_button);
        mLoginPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticAdapter.sendButtonEvent(new ButtonParamsPull("b_Phone_Ok"));
                next();
            }
        });

        /*mLoginPasswordHint = (TextView)view.findViewById(R.id.login_password_hint);
        mLoginPasswordHint.setText(R.string.auth_create_password);*/

        /*mChangePasswordButton = (Button)view.findViewById(R.id.login_forgot_password);
        mChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticAdapter.sendButtonEvent(new ButtonParamsPull("b_LoginPin_ForgotPin"));
                if (mListener != null) {
                    mListener.onPasswordChange();
                }
            }
        });*/

        return view;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            if (mListener == null) {
                mListener = (LoginPasswordFragmentInteractionListener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LoginPasswordFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void next() {
        LoginActivity.AuthState state = PreferencesHelper.getInstance().getAuthState();

        //mChangePasswordButton.setEnabled(false);

        if (state == LoginActivity.AuthState.SIGNUP || state == LoginActivity.AuthState.CHANGE_PIN) {

            // первый ввод пинкода
            if (pinCodeFirst == null) {
                if (state == LoginActivity.AuthState.SIGNUP) {
                    StatisticAdapter.sendButtonEvent(new ButtonParamsPull("e_CreatePin"));
                } else {
                    StatisticAdapter.sendButtonEvent(new ButtonParamsPull("e_ChangeCreatePin"));
                }
                createTaskRepeatPinInput();
                return;

            // на втором вводе пинкода пинкоды не совпали
            } else if (!mPinCodeView.getText().toString().equalsIgnoreCase(pinCodeFirst)) {
                if (state == LoginActivity.AuthState.SIGNUP) {
                    StatisticAdapter.sendButtonEvent(new ButtonParamsPull("e_ConfirmPin_NotEquals"));
                } else {
                    StatisticAdapter.sendButtonEvent(new ButtonParamsPull("e_ChangeConfirmPin_NotEquals"));
                }
                //mLoginPasswordHint.setText(R.string.auth_create_password_repeat_fail);
                mPinCodeView.setText(null);
                pinCodeFirst = null;
                //mChangePasswordButton.setEnabled(true);

                createTaskReturnToFirstPinInput();
                return;
            }
        }

        if (mListener != null) {
            mListener.onPasswordSendStart();
        }
        if (state == LoginActivity.AuthState.SIGNIN) {
            StatisticAdapter.sendButtonEvent(new ButtonParamsPull("e_signin_start"));
            signin();
        } else if (state == LoginActivity.AuthState.SIGNUP) {
            StatisticAdapter.sendButtonEvent(new ButtonParamsPull("e_signup_start"));
            signup();
        } else if (state == LoginActivity.AuthState.CHANGE_PIN) {
            //changePin();
        }
    }

    private void signup() {
        String login = PreferencesHelper.getInstance().getLogin();
        User.signup(getActivity(), login, mPinCodeView.getText().toString(), new ResponseCallback<AuthResponse>() {
            @Override
            public void onResponse(AuthResponse response) {
                StatisticAdapter.sendButtonEvent(new ButtonParamsPull("e_signup_success"));
                mListener.onPasswordSendEnd(response);
            }

            @Override
            public void onError(String error) {
                StatisticAdapter.sendButtonEvent(new ButtonParamsPull("e_signup_error"));
                mChangePasswordButton.setEnabled(true);
                if (mListener != null) {
                    //mLoginPasswordHint.setText(error);
                    mPinCodeView.setText(null);
                    mListener.onPasswordSendError(error);
                }
            }
        });
    }

    private void signin() {
        String login = PreferencesHelper.getInstance().getLogin();
        User.signin(getActivity(), login, mPinCodeView.getText().toString(), new ResponseCallback<AuthResponse>() {
            @Override
            public void onResponse(AuthResponse response) {
                if (mListener != null) {
                    StatisticAdapter.sendButtonEvent(new ButtonParamsPull("e_signin_success"));
                    mListener.onPasswordSendEnd(response);
                }
            }

            @Override
            public void onError(String error) {
                StatisticAdapter.sendButtonEvent(new ButtonParamsPull("e_signin_error"));
                mChangePasswordButton.setEnabled(true);
                if (mListener != null) {
                    //mLoginPasswordHint.setText(error);
                    mPinCodeView.setText(null);
                    mListener.onPasswordSendError(error);
                }
            }
        });
    }

    private void createTaskReturnToFirstPinInput() {
        createTaskReturnToFirstPinInput(5);
    }

    private void createTaskReturnToFirstPinInput(int secondsToStart) {
        ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
        worker.schedule(new Runnable() {

            @Override
            public void run() {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        //mLoginPasswordHint.setText(R.string.auth_create_password);
                        mListener.onPasswordSendError(null);
                    }
                });
            }
        }, secondsToStart, TimeUnit.SECONDS);
    }

    private void createTaskRepeatPinInput() {
        ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
        worker.schedule(new Runnable() {
            @Override
            public void run() {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        pinCodeFirst = mPinCodeView.getText().toString();
                        //mLoginPasswordHint.setText(R.string.auth_create_password_repeat);
                        mPinCodeView.setText(null);
                    }
                });
            }
        }, 500, TimeUnit.MILLISECONDS);
    }

    public interface LoginPasswordFragmentInteractionListener {
        void onPasswordSendStart();
        void onPasswordSendEnd(AuthResponse response);
        void onPasswordSendError(String error);
        void onPasswordChange();
    }

}

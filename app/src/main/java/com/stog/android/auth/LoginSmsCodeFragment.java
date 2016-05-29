package com.stog.android.auth;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stog.android.R;
import com.stog.android.statistic.ButtonParamsPull;
import com.stog.android.statistic.StatisticAdapter;
import com.stog.android.storage.PreferencesHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginSmsCodeFragment extends Fragment {

    private TextView mHint;
    private EditText mDigitCodeView;
    private Button mResendButton, mCancelButton;

    public final static String SMS_KEY = "stog";

    private LoginSmsCodeFragmentInteractionListener mListener;


    public static LoginSmsCodeFragment newInstance() {
        LoginSmsCodeFragment f = new LoginSmsCodeFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_login_sms_code, null);

        mHint = (TextView)view.findViewById(R.id.login_sms_hint);
        mDigitCodeView = (EditText)view.findViewById(R.id.login_code);

        mCancelButton = (Button)view.findViewById(R.id.login_cancel_operation_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticAdapter.sendButtonEvent(new ButtonParamsPull("b_RegSms_SendSmsCancel"));
                PreferencesHelper.getInstance().clearChangePinState();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        mResendButton = (Button)view.findViewById(R.id.auth_button_resend_sms);
        mResendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticAdapter.sendButtonEvent(new ButtonParamsPull("b_RegSms_SendSmsAgain"));
                if (mListener != null) {
                    mListener.onCodeNeedResend();
                }
            }
        });

        setVisibleButtonFromAuthState();
        return view;
    }

    // метод прячет согласие на получение КИ и иже с ними
    private void setVisibleButtonFromAuthState() {
        LoginActivity.AuthState state = PreferencesHelper.getInstance().getAuthState();
        if (state == LoginActivity.AuthState.CHANGE_PIN) {
            mCancelButton.setVisibility(View.VISIBLE);
        } else {
            mCancelButton.setVisibility(View.GONE);
        }
    }

    private void next(){
        if (mListener != null) {
            mListener.onCodeSendStart();
        }
        LoginActivity.AuthState state = PreferencesHelper.getInstance().getAuthState();
        StatisticAdapter.sendButtonEvent(new ButtonParamsPull("e_RegSms"));
        smsVerify();

    }

    void smsVerify() {
        //
    }



    @Override
    public void onStart() {
        super.onStart();
        StatisticAdapter.sendPageEvent(new ButtonParamsPull("p_RegSms"));
    }

    @Override
    public void onStop() {
        super.onStop();
        StatisticAdapter.endPageEvent(new ButtonParamsPull("p_RegSms"));
    }

    public interface LoginSmsCodeFragmentInteractionListener {
        void onCodeSendStart();
        void onNewRegisterToken(String token);
        void onCodeSendEnd(String code, LoginActivity.AuthState state);
        void onCodeSendError(String error);
        void onCodeNeedResend();
        void onCodeWasResent();
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity activity = getActivity();
        if(activity!=null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.provider.Telephony.SMS_RECEIVED");
            activity.registerReceiver(mSmsReceiver, filter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Activity activity = getActivity();
        if(activity!=null) {
            activity.unregisterReceiver(mSmsReceiver);
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            if (mListener == null) {
                mListener = (LoginSmsCodeFragmentInteractionListener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LoginSmsCodeFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void onSmsCodeReceived(int code) {
        mDigitCodeView.setText(null);
        char[] codeAsArray = Integer.toString(code).toCharArray();
        for (char _char: codeAsArray) {
            int number = Integer.parseInt(String.valueOf(_char));
            //mKeyboard.emulateTap(number);
        }
    }

    protected BroadcastReceiver mSmsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                Bundle extras = intent.getExtras();
                if(extras != null) {
                    Object[] pduArray = (Object[]) extras.get("pdus");
                    if (pduArray != null) {
                        SmsMessage[] messages = new SmsMessage[pduArray.length];
                        for (int i = 0; i < pduArray.length; i++) {
                            messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
                        }
                        String sms_from = messages[0].getDisplayOriginatingAddress();
                        if (sms_from.equals(SMS_KEY)) {
                            String textMessage = messages[0].getMessageBody();
                            Pattern p = Pattern.compile("\\d{4}");
                            Matcher m = p.matcher(textMessage);
                            if (m.find(0)) {
                                int verifyCode = Integer.parseInt(m.group(0));
                                onSmsCodeReceived(verifyCode);
                            }
                        }
                    }
                }
            }
        }
    };


}

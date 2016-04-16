package com.stog.android.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.stog.android.storage.PreferencesHelper;

import java.util.concurrent.ExecutionException;

public class GcmTokenHelper {

    public String generateGcmToken(final Context context) throws ExecutionException, InterruptedException {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String token = null;
                /*try {
                    InstanceID instanceID = InstanceID.getInstance(context);
                    String senderId =  context.getString(R.string.gcm_defaultSenderId);
                    token = instanceID.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                } catch (final IOException e) {
                    Log.e("GcmTokenHelper", e.getMessage(), e);
                }*/
                return token;
            }
        };
        return task.execute().get();
    }

    public String getGcmToken(Context context){
        String gcmToken = PreferencesHelper.getGcmToken();
        if(gcmToken==null){
            try {
                gcmToken = generateGcmToken(context);
            } catch (ExecutionException | InterruptedException e) {
                Log.e("GcmTokenHelper", e.getMessage(), e);
            }
        }
        return gcmToken;
    }
}

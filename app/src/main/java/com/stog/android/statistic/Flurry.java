package com.stog.android.statistic;


import java.util.HashMap;
import java.util.Map;

public class Flurry extends StaticticEventAdapter {

    private static Flurry instance = null;

    public static Flurry newInstance() {
        if(null == instance) {
            instance = new Flurry();
        } return instance;
    }

    public static Flurry getInstance() {
        if(null == instance) {
            throw new IllegalStateException("You must call getInstance(Context context)");
        } return instance;
    }

    public Flurry() {}

    @Override
    public void sendButtonEvent(ButtonParamsPull params) {
        Map<String, String> articleParams = new HashMap<>();
        if(params instanceof ErrorParamsPull){
            articleParams.put("errorType", ((ErrorParamsPull)params).getErrorType());
            articleParams.put("errorDescription", ((ErrorParamsPull)params).getErrorDescription());
        }
        //FlurryAgent.logEvent(params.getId(), articleParams);
    }

    @Override
    public void sendPageEvent(String id) {
        if(id != null) {
            Map<String, String> articleParams = new HashMap<>();
            //FlurryAgent.logEvent(id, articleParams, true);
        }
    }

    @Override
    public void endPageEvent(String id) {
        //FlurryAgent.endTimedEvent(id);
    }


    @Override
    public void setUser(String hash) {
        //FlurryAgent.setUserId(hash);
    }
}

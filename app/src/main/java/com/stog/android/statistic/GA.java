package com.stog.android.statistic;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class GA {

    private static GA instance = null;
    private Context context = null;
    private static final String TAG = "mobile-ga-stat";
    private GoogleAnalytics analytics;
    private Tracker tracker;

    public static GA getInstance(Context context) {
        if(null == instance) {
            instance = new GA(context);
            instance.context = context;
        } return instance;
    }

    public GA(Context context) {
        this.context = context;
        analytics = GoogleAnalytics.getInstance(context);
    }

    public Tracker getTracker() {
        if(tracker == null) {
            //tracker = analytics.newTracker(R.xml.app_tracker);
            tracker.enableAdvertisingIdCollection(true);
        }
        return tracker;
    }

    public void sendScreenName(String screenName) {
        Tracker t = getTracker();
        t.setScreenName(screenName);
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void sendButtonEvent(String category, String action) {
        Tracker t = getTracker();
        t.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .build());
    }

    public void sendPageEvent(String id) {

    }
}

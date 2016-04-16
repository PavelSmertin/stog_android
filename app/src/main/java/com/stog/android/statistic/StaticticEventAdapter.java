package com.stog.android.statistic;

abstract public class StaticticEventAdapter {
    abstract public void sendButtonEvent(ButtonParamsPull params);
    abstract public void sendPageEvent(String id);
    abstract public void endPageEvent(String id);
    abstract public void setUser(String hash);
}

package com.stog.android.statistic;

import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ButtonParamsPull {
    private String id;

    Map<String, Object> eventValue = new HashMap<>();

    public ButtonParamsPull(@Nullable String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getEventValue() {
        return eventValue;
    }

    public void setEventValue(Map<String, Object> eventValue) {
        this.eventValue = eventValue;
    }

}

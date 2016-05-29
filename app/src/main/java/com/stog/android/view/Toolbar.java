package com.stog.android.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.stog.android.R;


public class Toolbar extends android.support.v7.widget.Toolbar {

    public static android.support.v7.widget.Toolbar setup(AppCompatActivity activity) {
        android.support.v7.widget.Toolbar mActionBarToolbar = getToolbar(activity);
        android.widget.TextView mTitle = getTitle(mActionBarToolbar);
        mTitle.setText(activity.getTitle());
        activity.setSupportActionBar(mActionBarToolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        return mActionBarToolbar;
    }

    public static void setTitleColor(AppCompatActivity activity, int color){
        android.support.v7.widget.Toolbar mActionBarToolbar = getToolbar(activity);
        android.widget.TextView mTitle = getTitle(mActionBarToolbar);
        mTitle.setTextColor(activity.getResources().getColor(color));
    }

    public static android.widget.TextView getTitle(android.support.v7.widget.Toolbar toolbar){
        return (android.widget.TextView) toolbar.findViewById(R.id.toolbar_title);
    }

    public static android.support.v7.widget.Toolbar getToolbar(AppCompatActivity activity){
        return (android.support.v7.widget.Toolbar) activity.findViewById(R.id.toolbar_actionbar);
    }

    public Toolbar(Context context) {
        super(context);
        init();
    }

    public Toolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Toolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.toolbar, this);
    }
}

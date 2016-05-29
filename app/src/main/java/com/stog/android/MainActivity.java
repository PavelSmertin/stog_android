package com.stog.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.stog.android.dummy.DummyContent;
import com.stog.android.view.Toolbar;

public class MainActivity extends AppCompatActivity  implements TagFragment.OnListFragmentInteractionListener{

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = getTitle();

        Toolbar.setup(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = TagFragment.newInstance(1);

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}

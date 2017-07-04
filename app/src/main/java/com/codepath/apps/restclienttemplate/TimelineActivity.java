package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsPagerAdapter;

public class TimelineActivity extends AppCompatActivity {

    private final int REQUEST_CODE_A = 10;
    private final int REQUEST_CODE_B = 20;

    TweetsListFragment fragmentTweetsList;
    ViewPager viewPager;
    TweetsPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher_twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("");

        // get the view pager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new TweetsPagerAdapter(getSupportFragmentManager(), this);

        // set up the adapter for the pager
        viewPager.setAdapter(adapter);

        // setup the Tab Layout to use the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    public void onComposeAction(MenuItem miCompose) {
        Intent intent = new Intent(this, ComposeActivity.class);
        intent.putExtra("reply", false);
        startActivityForResult(intent, REQUEST_CODE_A);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TweetsListFragment fragment = (TweetsListFragment) adapter.getRegisteredFragment(viewPager.getCurrentItem());
        fragment.activityResult(requestCode, resultCode, data);
    }

    public void onProfileView(MenuItem miProfile) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}

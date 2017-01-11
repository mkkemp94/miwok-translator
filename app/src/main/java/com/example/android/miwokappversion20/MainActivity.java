package com.example.android.miwokappversion20;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find view pager from xml
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create adapter for view pager
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(MainActivity.this, getSupportFragmentManager());

        // Set adapter onto view pager
        viewPager.setAdapter(adapter);

        // Get the tab layout and give the view pager to it
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}

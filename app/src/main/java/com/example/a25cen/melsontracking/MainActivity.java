package com.example.a25cen.melsontracking;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private SectionsPageAdapter pageAdapter;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Starting");

        pageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        //Set up the ViewPager with the sections adapter
        mViewPager = findViewById(R.id.container);
        setupViewAdapter(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



    }

    private void setupViewAdapter(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new MovieFragment(), "Movies");
        adapter.addFragment(new PeopleFragment(), "People");
        viewPager.setAdapter(adapter);
    }


}

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
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Starting");

        pageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        //Set up the ViewPager with the sections adapter
        mViewPager = findViewById(R.id.container);
        setupViewAdapter(mViewPager);

        //Creating the tablayout to allow for changing between the fragments
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setupIcons();

    }

    /*
        setupViewAdapter
        Function that will setup the view adapter with the fragments that will be used in the application
        Pre: viewPager --> The layoutmanager that allows for the user to swipe left and right between the tabs of the application
        Post: None
     */
    private void setupViewAdapter(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new MovieFragment(), "Movies");
        adapter.addFragment(new PeopleFragment(), "People");
        viewPager.setAdapter(adapter);
        return;
    }

    public void setupIcons(){
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_local_movies_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_person_white_24dp);
        return;
    }


}

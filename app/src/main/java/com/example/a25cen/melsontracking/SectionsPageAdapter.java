package com.example.a25cen.melsontracking;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Innocent on 11/3/17.
 */

public class SectionsPageAdapter extends FragmentPagerAdapter{

    private final ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    private final ArrayList<String> fragmentTitles = new ArrayList<String>();

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void  addFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        fragmentTitles.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }
}

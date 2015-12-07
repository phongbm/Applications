package com.phongbm.applications;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> pages;
    private String[] titles;

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        pages = new ArrayList<>();
        pages.add(new InstalledFragment());
        pages.add(new SystemAppFragment());
        titles = new String[]{"Installed", "System App"};
    }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
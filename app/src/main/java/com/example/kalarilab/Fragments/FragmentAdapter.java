package com.example.kalarilab.Fragments;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.kalarilab.Activities.MainActivity;

public class FragmentAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;
    private MainActivity mainActivity;
    private FragmentManager fragmentManager;
    public FragmentAdapter(FragmentManager fragmentManager, MainActivity mainActivity) {
        super(fragmentManager);
        this.mainActivity = mainActivity;
        this.fragmentManager = fragmentManager;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        //getting the right fragment for the index
        switch (position) {

            case 1:
                return LevelsFragment.newInstance("1", "Page #2");
            case 2:
                return ProfileFragment.newInstance("2", "Page #3");


            default:
                return HomeFragment.newInstance("0", "Page #1");
        }

    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    // Force a refresh of the page when a different fragment is displayed
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}

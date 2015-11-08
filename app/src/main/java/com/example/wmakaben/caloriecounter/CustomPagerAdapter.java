package com.example.wmakaben.caloriecounter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CustomPagerAdapter extends FragmentStatePagerAdapter {
    public static final int NUMTABS = 2;
    protected Context mContext;

    public CustomPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    // This method returns the fragment associated with
    // the specified position.
    //
    // It is called when the Adapter needs a fragment
    // and it does not exists.
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new CameraFragment();
            case 1:
                return new HistoryFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUMTABS;
    }
}
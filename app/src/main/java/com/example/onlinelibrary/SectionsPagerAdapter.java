package com.example.onlinelibrary;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.example.onlinelibrary.fragments.AvailableFragment;
import com.example.onlinelibrary.fragments.HistoryFragment;
import com.example.onlinelibrary.fragments.RentedFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    Context mContext;
    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new AvailableFragment();
                break;
            case 1:
                fragment = new RentedFragment();
                break;
            case 2:
                fragment = new HistoryFragment();
                break;
        }
        return fragment;
    }
    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.available);
            case 1:
                return mContext.getString(R.string.rented);
            case 2:
                return mContext.getString(R.string.history);
        }
        return null;
    }
}
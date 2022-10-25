package com.example.fuelme.ui.mainscreen.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.fuelme.ui.mainscreen.fragments.AllStationsFragment;
import com.example.fuelme.ui.mainscreen.fragments.FavouriteStationsFragment;

/*
 *  IT19014128
 *  A.M.W.W.R.L. Wataketiya
 *
 * Page adapter for main screen view pager
 * */


public class PageAdapter extends FragmentPagerAdapter {

    private int tabsCount;

    public PageAdapter(@NonNull FragmentManager fm, int behavior, int tabsCount) {
        super(fm, behavior);
        this.tabsCount = tabsCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AllStationsFragment();
            case 1:
                return new FavouriteStationsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.tabsCount;
    }
}

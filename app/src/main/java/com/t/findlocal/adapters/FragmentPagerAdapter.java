package com.t.findlocal.adapters;

import com.t.findlocal.activities.CategoriesFragment;
import com.t.findlocal.activities.TextSearchFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentPagerAdapter extends androidx.fragment.app.FragmentPagerAdapter {
    private String tabTitles[] = new String[] { "Search", "Categories"};
    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new TextSearchFragment();
        }
        else{
            return new CategoriesFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}

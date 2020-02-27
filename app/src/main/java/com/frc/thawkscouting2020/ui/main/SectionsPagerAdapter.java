package com.frc.thawkscouting2020.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.frc.thawkscouting2020.AutoFragment;
import com.frc.thawkscouting2020.EndgameFragment;
import com.frc.thawkscouting2020.R;
import com.frc.thawkscouting2020.TeleOpFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        // getItem is called to instantiate the fragment for the given page.
        // Return a Fragment
        switch (position) {
            case 0:
                fragment = new AutoFragment();
                break;
            case 1:
                fragment = new TeleOpFragment();
                break;
            case 2:
                fragment = new EndgameFragment();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}

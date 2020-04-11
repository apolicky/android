package org.policky.ghotaapp2019v2.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.policky.ghotaapp2019v2.Contacts;
import org.policky.ghotaapp2019v2.OnlinePhotos;
import org.policky.ghotaapp2019v2.Organization;
import org.policky.ghotaapp2019v2.Other;
import org.policky.ghotaapp2019v2.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_organization, R.string.tab_contacts, R.string.tab_online_photos, R.string.tab_packing};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new Organization();
                break;
            case 1:
                fragment = new Contacts();
                break;
            case 2:
                fragment = new OnlinePhotos();
                break;
            case 3:
                fragment = new Other();
                break;

                default:
                    fragment = null;
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
        // Show 2 total pages.
        return 4;
    }
}
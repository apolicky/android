package org.policky.ghotaapp2019v2.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import org.policky.ghotaapp2019v2.ConfigManager;
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
    private static int[] TABS_USED, TAB_TITLES;
    private final Context mContext;
    private ConfigManager CM;

    public SectionsPagerAdapter(Context context, FragmentManager fm, ConfigManager CM_) {
        super(fm);
        clear(fm);
        mContext = context;
        CM = CM_;
        TABS_USED = new int[]{0,1,2,3};
        TAB_TITLES = new int[]{R.string.tab_organization,R.string.tab_contacts,R.string.tab_others,R.string.tab_online_photos};
        whatTabs();
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment = null;

        switch (TABS_USED[position]) {
            case 0:
                fragment = new Organization(CM);
                break;
            case 1:
                fragment = new Contacts(CM);
                break;
            case 2:
                fragment = new Other(CM);
                break;
            case 3:
                fragment = new OnlinePhotos(CM);
                break;

            default:
                fragment = null;
        }

        return fragment;

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
       return mContext.getResources().getString(TAB_TITLES[TABS_USED[position]]);
    }

    @Override
    public int getCount() {
        return TABS_USED.length;
    }

    private void whatTabs(){
        int[] a = CM.getUsedTabs();
        if(a != null && a.length > 0){
            TABS_USED = a;
        }
    }

    private void clear(FragmentManager fm) {
        FragmentTransaction transaction = fm.beginTransaction();
        for (Fragment fragment : fm.getFragments()) {
            transaction.remove(fragment);
        }
        fm.getFragments().clear();
        transaction.commitNow();
    }
}
package cz.apolicky.summercampapp.ui.main;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import cz.apolicky.summercampapp.ConfigurationManager;
import cz.apolicky.summercampapp.MainActivity;
import cz.apolicky.summercampapp.R;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages. With respect to MainActivity
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static int[] TABS_USED, TAB_TITLES;
    private final MainActivity MA;
    private ConfigurationManager CM;

    public MainPagerAdapter(MainActivity ma, FragmentManager fm, ConfigurationManager CM_) {
        super(fm);
        clear(fm);
        MA = ma;
        CM = CM_;
        TABS_USED = new int[]{0,1,2,3};
        TAB_TITLES = new int[]{R.string.tab_organization,R.string.tab_contacts,R.string.tab_others,R.string.tab_online_photos};
        whatTabs();
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (TABS_USED[position]) {
            case 0:
//                fragment = new Organization(CM);
                return MA.getOrganizationFragment();
            case 1:
//                fragment = new Contacts(CM);
                return MA.getContactsFragment();
            case 2:
//                fragment = new Other(CM);
                return MA.getOtherFragment();
            case 3:
//                fragment = new OnlinePhotos(CM);
                return MA.getPhotosFragment();

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
       return MA.getString(TAB_TITLES[TABS_USED[position]]);
    }

    @Override
    public int getCount() {
        return TABS_USED.length;
    }

    private void whatTabs(){
        int[] a =  CM.getIntArray(MA.getString(R.string.tabs_used));
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
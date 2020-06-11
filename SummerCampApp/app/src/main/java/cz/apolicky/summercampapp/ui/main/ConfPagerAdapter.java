package cz.apolicky.summercampapp.ui.main;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import cz.apolicky.summercampapp.CreateConfiguration;
import cz.apolicky.summercampapp.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages. With respect to CreateConfiguration
 */
public class ConfPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_create_organization,R.string.tab_create_contacts,R.string.tab_create_others, R.string.tab_create_photos, R.string.tab_create_developer};
    private final CreateConfiguration CC;

    public ConfPagerAdapter(CreateConfiguration cc, FragmentManager fm) {
        super(fm);
        CC = cc;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position){
            case 0:
                return CC.getCreateOrganizationFragment();
            case 1:
                return CC.getCreateContactsFragment();
            case 2:
                return CC.getCreateOthersFragment();
            case 3:
                return CC.getCreatePhotosFragment();
            case 4:
                return CC.getCreateDeveloperFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return CC.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}
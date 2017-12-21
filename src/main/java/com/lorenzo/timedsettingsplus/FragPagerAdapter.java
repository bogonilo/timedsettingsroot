package com.lorenzo.timedsettingsplus;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.lorenzo.timedsettingsplus.fragments.Friday_fragment;
import com.lorenzo.timedsettingsplus.fragments.Monday_fragment;
import com.lorenzo.timedsettingsplus.fragments.Saturday_fragment;
import com.lorenzo.timedsettingsplus.fragments.Sunday_fragment;
import com.lorenzo.timedsettingsplus.fragments.Thursday_fragment;
import com.lorenzo.timedsettingsplus.fragments.Tuesday_fragment;
import com.lorenzo.timedsettingsplus.fragments.Wednesday_fragment;

/**
 * Created by lorenzo on 31/10/15.
 */

public class FragPagerAdapter extends SmartFragmentStatePagerAdapter{

    Context context;

    public FragPagerAdapter(FragmentManager manager, Context c)
    {
        super(manager);
        context=c;

    }

    @Override
    public Fragment getItem(int index) {
        Fragment fragment = null;

        switch (index) {
            case 0:
                fragment = new Sunday_fragment();
                getPageTitle(0);
                break;
            case 1:
                fragment = new Monday_fragment();
                getPageTitle(1);
                break;
            case 2:
                fragment = new Tuesday_fragment();
                getPageTitle(2);
                break;
            case 3:
                fragment = new Wednesday_fragment();
                getPageTitle(3);
                break;
            case 4:
                fragment = new Thursday_fragment();
                getPageTitle(4);
                break;
            case 5:
                fragment = new Friday_fragment();
                getPageTitle(5);
                break;
            case 6:
                fragment = new Saturday_fragment();
                getPageTitle(6);
                break;
        }


        return fragment;
    }

    @Override
    public int getCount()
    {
        return 7;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        String title="";
        switch (position) {
            case 0 : title = context.getResources().getString(R.string.Sun);
                break;
            case 1 : title = context.getResources().getString(R.string.Mon);
                break;
            case 2 : title = context.getResources().getString(R.string.Tue);
                break;
            case 3 : title = context.getResources().getString(R.string.Wed);
                break;
            case 4 : title = context.getResources().getString(R.string.Thu);
                break;
            case 5 : title = context.getResources().getString(R.string.Fri);
                break;
            case 6 : title = context.getResources().getString(R.string.Sat);
                break;
        }

        return title;
}

}

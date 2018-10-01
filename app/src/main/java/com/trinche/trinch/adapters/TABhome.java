package com.trinche.trinch.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.trinche.trinch.subfragments.SFcategories;
import com.trinche.trinch.subfragments.SFcountries;
import com.trinche.trinch.subfragments.SFnews;

public class TABhome extends FragmentStatePagerAdapter {

    private static final int TAB_1 = 0;
    private static final int TAB_2 = 1;
    private static final int TAB_3 = 2;

    private static final int NB_TABS = 3;

    public TABhome(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case TAB_1:
                SFnews tab1 = new SFnews();
                return tab1;

            case TAB_2:
                SFcountries tab2 = new SFcountries();
                return tab2;

            case TAB_3:
                SFcategories tab3 = new SFcategories();
                return tab3;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NB_TABS;
    }
}

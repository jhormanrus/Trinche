package com.trinche.app.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loicteillard.easytabs.EasyTabs;
import com.trinche.app.R;
import com.trinche.app.adapters.TABhome;

public class LOhome extends Fragment {

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_home, container, false);

        EasyTabs easyTabs = v.findViewById(R.id.easytabs);
        ViewPager viewpager = v.findViewById(R.id.viewpager);
        TABhome pagerAdapter = new TABhome(getFragmentManager());
        viewpager.setAdapter(pagerAdapter);

        easyTabs.setViewPager(viewpager, 0);

        easyTabs.setPagerListener(new EasyTabs.PagerListener() {
            @Override
            public void onTabSelected(int position) {
            }
        });

        return v;
    }
}

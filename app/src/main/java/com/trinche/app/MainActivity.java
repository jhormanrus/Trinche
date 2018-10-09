package com.trinche.app;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;
import com.trinche.app.fragments.LOhome;
import com.trinche.app.fragments.LOnotifications;
import com.trinche.app.fragments.LOprofile;
import com.trinche.app.fragments.LOranking;
import com.trinche.app.fragments.LOrecipe;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home){
                    LOhome f = new LOhome();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                } else if (tabId == R.id.tab_ranking){
                    LOranking f = new LOranking();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                } else if (tabId == R.id.tab_recipe){
                    LOrecipe f = new LOrecipe();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                } else if (tabId == R.id.tab_notifications){
                    BottomBarTab nearby = bottomBar.getTabWithId(R.id.tab_notifications);
                    nearby.removeBadge();
                    LOnotifications f = new LOnotifications();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                } else if (tabId == R.id.tab_profile){
                    LOprofile f = new LOprofile();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                }
            }
        });

        BottomBarTab nearby = bottomBar.getTabWithId(R.id.tab_notifications);
        nearby.setBadgeCount(4);
    }

    @Override
    public void onBackPressed() {
        //
    }
}

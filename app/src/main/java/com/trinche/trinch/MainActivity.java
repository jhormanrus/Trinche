package com.trinche.trinch;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.trinche.trinch.fragments.LOhome;
import com.trinche.trinch.fragments.LOnotifications;
import com.trinche.trinch.fragments.LOprofile;
import com.trinche.trinch.fragments.LOranking;
import com.trinche.trinch.fragments.LOrecipe;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
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
                    LOnotifications f = new LOnotifications();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                } else if (tabId == R.id.tab_profile){
                    LOprofile f = new LOprofile();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                }
            }
        });
    }
}

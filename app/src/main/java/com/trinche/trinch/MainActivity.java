package com.trinche.trinch;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends AppCompatActivity {

    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.menu_bar, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int i) {
                if (i == R.id.navigation_home) {
                    LOhome f = new LOhome();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                } else if (i == R.id.navigation_ranking) {
                    LOranking f = new LOranking();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                } else if (i == R.id.navigation_recipe) {
                    LOrecipe f = new LOrecipe();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                } else if (i == R.id.navigation_notifications) {
                    LOnotifications f = new LOnotifications();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                } else if (i == R.id.navigation_profile) {
                    LOprofile f = new LOprofile();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                }
            }

            @Override
            public void onMenuTabReSelected(int menuItemId) {

            }
        });

        bottomBar.mapColorForTab(0, "#FFB475");
        bottomBar.mapColorForTab(1, "#C0FF75");
        bottomBar.mapColorForTab(2, "#7BFF75");
        bottomBar.mapColorForTab(3, "#75FFB4");
        bottomBar.mapColorForTab(4, "#75FFF9");

        BottomBarBadge unread;
        unread = bottomBar.makeBadgeForTabAt(3, "#FF0000", 13);
        unread.show();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }
}

package com.trinche.app;

import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.awesome.dialog.AwesomeCustomDialog;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.trinche.app.adapters.STEPcreate_recipe;
import com.trinche.app.fragments.LOhome;
import com.trinche.app.fragments.LOnotifications;
import com.trinche.app.fragments.LOprofile;
import com.trinche.app.fragments.LOranking;
import com.trinche.app.fragments.LOrecipe;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements StepperLayout.StepperListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_home: {
                        LOhome f = new LOhome();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                        break;
                    }
                    case R.id.tab_ranking: {
                        LOranking f = new LOranking();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                        break;
                    }
                    case R.id.tab_recipe:
                        System.out.println("poo");
                        /*new AwesomeCustomDialog(MainActivity.this).setTopColor(Color.parseColor("#FFB475")).setIcon(R.drawable.ic_twotone_receipt_24px).setIconTintColor(Color.parseColor("#FFFFFF"))
                                .setTitle("Crear receta").setView(R.layout.tiny_dialogcreaterecipe).configureView(new AwesomeCustomDialog.ViewConfigurator() {
                            @Override
                            public void configureView(@NotNull View view) {
                                StepperLayout stepper_create = (StepperLayout) view.findViewById(R.id.stepper_create);
                                stepper_create.setAdapter(new STEPcreate_recipe(getSupportFragmentManager(), getApplicationContext()));
                                stepper_create.setListener(MainActivity.this);
                            }
                        }).show();*/

                        break;
                    case R.id.tab_notifications: {
                        BottomBarTab nearby = bottomBar.getTabWithId(R.id.tab_notifications);
                        nearby.removeBadge();
                        LOnotifications f = new LOnotifications();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                        break;
                    }
                    case R.id.tab_profile: {
                        LOprofile f = new LOprofile();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                        break;
                    }
                }
            }
        });

        BottomBarTab nearby = bottomBar.getTabWithId(R.id.tab_notifications);
        nearby.setBadgeCount(4);
    }

    @Override
    public void onBackPressed() {
        // Nothing happens here
    }

    @Override
    public void onCompleted(View completeButton) {
        System.out.println("completed");
    }

    @Override
    public void onError(VerificationError verificationError) {
        System.out.println("Error: " + verificationError.getErrorMessage());
    }

    @Override
    public void onStepSelected(int newStepPosition) {
        System.out.println("Step: " + newStepPosition);
    }

    @Override
    public void onReturn() {
        System.out.println("return");
    }
}

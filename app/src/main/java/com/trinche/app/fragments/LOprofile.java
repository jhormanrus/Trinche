package com.trinche.app.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trinche.app.R;

import at.markushi.ui.CircleButton;

public class LOprofile extends Fragment {

    CircleButton settingsBTN;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_profile, container, false);

        settingsBTN = (CircleButton) v.findViewById(R.id.settingsBTN);
        settingsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
                getActivity().finish();
            }
        });

        return v;
    }
}

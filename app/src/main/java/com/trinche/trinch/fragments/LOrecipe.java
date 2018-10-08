package com.trinche.trinch.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stepstone.stepper.StepperLayout;
import com.trinche.trinch.R;
import com.trinche.trinch.adapters.STEPcreate_recipe;

public class LOrecipe extends Fragment {

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_recipe, container, false);

        StepperLayout stepper_create = (StepperLayout) v.findViewById(R.id.stepper_create);
        stepper_create.setAdapter(new STEPcreate_recipe(getFragmentManager(), getContext()));
        return v;
    }
}

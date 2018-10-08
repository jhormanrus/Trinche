package com.trinche.trinch.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;
import com.trinche.trinch.subfragments.SFstep_create_1;

public class STEPcreate_recipe extends AbstractFragmentStepAdapter {


    public STEPcreate_recipe(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        final SFstep_create_1 step = new SFstep_create_1();
        Bundle b = new Bundle();
        b.putInt("maincra", position);
        step.setArguments(b);
        return step;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        if (position == 0) {
            return new StepViewModel.Builder(context).setTitle("Descripción").create();
        } else if (position == 1) {
            return new StepViewModel.Builder(context).setTitle("Ingredientes").create();
        } else if (position == 2) {
            return new StepViewModel.Builder(context).setTitle("Pasos").create();
        } else{
            return null;
        }
    }
}

package com.trinche.app.adapters;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;
import com.trinche.app.subfragments.SFstep_create_1;
import com.trinche.app.subfragments.SFstep_create_2;
import com.trinche.app.subfragments.SFstep_create_3;

public class STEPcreate_recipe extends AbstractFragmentStepAdapter {

    public STEPcreate_recipe(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        switch (position) {
            case 0: {
                final SFstep_create_1 step = new SFstep_create_1();
                /*Bundle b = new Bundle();
                b.putInt("maincra", position);
                step.setArguments(b);*/
                return step;
            }
            case 1: {
                final SFstep_create_2 step = new SFstep_create_2();
                return step;
            }
            case 2: {
                final SFstep_create_3 step = new SFstep_create_3();
                return step;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        switch (position) {
            case 0:
                return new StepViewModel.Builder(context).setTitle("Descripción").setSubtitle("Ingresa los datos\nbásicos de tu receta").setBackButtonLabel("Cancelar").setEndButtonLabel("Siguiente").create();
            case 1:
                return new StepViewModel.Builder(context).setTitle("Pasos").setSubtitle("Crea los pasos de\ntu receta").setBackButtonLabel("Atrás").setEndButtonLabel("Crear Receta").create();
            case 2:
                return new StepViewModel.Builder(context).setTitle("Ingredientes").setSubtitle("Visualiza los ingredientes\nde tu receta").setBackButtonLabel("Atrás").setEndButtonLabel("Crear Receta").create();
        }
        return null;
    }
}

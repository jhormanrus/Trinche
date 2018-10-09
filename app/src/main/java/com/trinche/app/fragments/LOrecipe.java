package com.trinche.app.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.trinche.app.R;
import com.trinche.app.adapters.STEPcreate_recipe;

public class LOrecipe extends Fragment implements StepperLayout.StepperListener {

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_recipe, container, false);

        StepperLayout stepper_create = (StepperLayout) v.findViewById(R.id.stepper_create);
        stepper_create.setAdapter(new STEPcreate_recipe(getFragmentManager(), getContext()));
        stepper_create.setListener(this);
        return v;
    }

    @Override
    public void onCompleted(View completeButton) {
        Toast.makeText(getContext(), "onCompleted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(VerificationError verificationError) {
        Toast.makeText(getContext(), "onError! -> " + verificationError.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepSelected(int newStepPosition) {
        Toast.makeText(getContext(), "onStepSelected! -> " + newStepPosition, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReturn() {
        Toast.makeText(getContext(), "you returned", Toast.LENGTH_LONG).show();
    }
}

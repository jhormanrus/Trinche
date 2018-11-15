package com.trinche.app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;
import com.trinche.app.subfragments.SFshow_step;

import org.json.JSONArray;
import org.json.JSONException;

public class ShowRecipe extends AppCompatActivity {

    StepperLayout stepper_recipeSL;
    JSONArray steps;
    String array_steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe);

        final Intent intent = getIntent();
        array_steps = intent.getStringExtra("steps");
        try {
            steps = new JSONArray(array_steps);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        stepper_recipeSL = (StepperLayout) findViewById(R.id.stepper_recipeSL);
        stepper_recipeSL.setAdapter(new STEPshow_step(getSupportFragmentManager(), this));
    }

    public class STEPshow_step extends AbstractFragmentStepAdapter {

        public STEPshow_step(@NonNull FragmentManager fm, @NonNull Context context) {
            super(fm, context);
        }

        @Override
        public Step createStep(int position) {
            final SFshow_step step = new SFshow_step();
            Bundle b = new Bundle();
            try {
                b.putString("jsonObject", steps.getJSONObject(position).toString());
                b.putInt("position", position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            step.setArguments(b);
            return step;
        }

        @Override
        public int getCount() {
            return steps.length();
        }

        @NonNull
        @Override
        public StepViewModel getViewModel(@IntRange(from = 0) int position) {
            StepViewModel.Builder builder = new StepViewModel.Builder(context);
            int last = steps.length()-1;
            if (last == 0) {
                return new StepViewModel.Builder(context).setBackButtonLabel("Volver").setEndButtonLabel("Finalizar").create();
            } else if (position == 0) {
                return new StepViewModel.Builder(context).setBackButtonLabel("Volver").setEndButtonLabel("Siguiente").create();
            } else if (position == last) {
                return new StepViewModel.Builder(context).setBackButtonLabel("Atrás").setEndButtonLabel("Finalizar").create();
            }
            return new StepViewModel.Builder(context).setBackButtonLabel("Atrás").setEndButtonLabel("Siguiente").create();
        }
    }
}

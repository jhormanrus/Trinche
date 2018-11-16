package com.trinche.app.subfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.trinche.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SFshow_step extends Fragment implements BlockingStep, View.OnClickListener {

    JSONObject step;
    Integer position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            step = new JSONObject(getArguments().getString("jsonObject"));
            position = getArguments().getInt("position");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.subfrag_show_step, container, false);

        TextView step_nameTV = (TextView) v.findViewById(R.id.step_nameTV);
        TextView step_ingredientsTV = (TextView) v.findViewById(R.id.step_ingredientsTV);
        TextView step_descriptionTV = (TextView) v.findViewById(R.id.step_descriptionTV);
        ImageView step_imageIV = (ImageView) v.findViewById(R.id.step_imageIV);
        try {
            step_nameTV.setText(step.getString("N_PASO") + ". " + step.getString("NOMBRE"));
            String array = step.getJSONArray("DETALLE_PASOS").toString();
            JSONArray ingredients = new JSONArray(array);
            String step_ingredients = "";
            for (int j = 0; j < ingredients.length(); j++) {
                JSONObject mono = ingredients.getJSONObject(j);
                Integer medida = mono.getInt("MEDIDA");
                String measure = "";
                if (medida == 0) {
                    measure = "gr.";
                } else if (medida == 1) {
                    measure = "kg.";
                } else if (medida == 2) {
                    measure = "ml.";
                } else if (medida == 3) {
                    measure = "l.";
                } else if (medida == 4) {
                    measure = "oz.";
                } else  if (medida == 5) {
                    measure = "unid.";
                }
                step_ingredients = step_ingredients + "- " + mono.getString("NOMBRE") + " " + mono.getString("PESO") + " " + measure + "\n";
            }
            step_ingredientsTV.setText(step_ingredients);
            step_descriptionTV.setText(step.getString("DESCRIPCION"));
            Glide.with(getActivity()).load("http://104.197.2.172/image/recipe/stepdown/" + step.getString("ID_PASOS")).apply(new RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE)).into(step_imageIV);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        getActivity().onBackPressed();
        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        if (position == 0) {
            getActivity().onBackPressed();
        }
        callback.goToPrevStep();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}

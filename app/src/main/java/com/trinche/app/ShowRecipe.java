package com.trinche.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.redbooth.WelcomeCoordinatorLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowRecipe extends AppCompatActivity {

    WelcomeCoordinatorLayout stepsWCL;
    int[] pages;
    JSONArray steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe);

        final Intent intent = getIntent();
        try {
            steps = new JSONArray(intent.getStringExtra("steps"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        stepsWCL = (WelcomeCoordinatorLayout) findViewById(R.id.stepsWCL);
        pages = new int[steps.length()];
        for (int i = 0; i < steps.length(); i++) {
            pages[i] = R.layout.tiny_show_recipe;
            System.out.println(pages[i]);
        }
        stepsWCL.addPage(pages);
        stepsWCL.setOnPageScrollListener(new WelcomeCoordinatorLayout.OnPageScrollListener() {
            @Override
            public void onScrollPage(View v, float progress, float maximum) { }

            @Override
            public void onPageSelected(View v, int pageSelected) {
                TextView step_nameTV = (TextView) v.findViewById(R.id.step_nameTV);
                TextView step_ingredientsTV = (TextView) v.findViewById(R.id.step_ingredientsTV);
                TextView step_descriptionTV = (TextView) v.findViewById(R.id.step_descriptionTV);
                ImageView step_imageIV = (ImageView) v.findViewById(R.id.step_imageIV);
                try {
                    JSONObject mini = steps.getJSONObject(pageSelected);
                    step_nameTV.setText(mini.getString("N_PASO") + ". " + mini.getString("NOMBRE"));
                    String array = mini.getJSONArray("DETALLE_PASOS").toString();
                    JSONArray ingredients = new JSONArray(array);
                    String step_ingredients = "";
                    for (int j = 0; j < ingredients.length(); j++) {
                        JSONObject mono = ingredients.getJSONObject(j);
                        step_ingredients = step_ingredients + "- " + mono.getString("NOMBRE") + " " + mono.getString("PESO") + "\n";
                    }
                    step_ingredientsTV.setText(step_ingredients);
                    step_descriptionTV.setText(mini.getString("DESCRIPCION"));
                    Glide.with(getApplicationContext()).load("http://104.197.2.172:8760/recipe/stepdown/" + mini.getString("ID_PASOS")).apply(new RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE)).into(step_imageIV);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

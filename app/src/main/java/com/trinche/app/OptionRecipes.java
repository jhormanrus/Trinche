package com.trinche.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.trinche.app.adapters.RVrecipes;

public class OptionRecipes extends AppCompatActivity {

    RecyclerView RVlayoutCustom;
    TextView layoutTV;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_recipes);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        // 1 = my recipes | 2 = recipes of book | 3 = recipes of country | 4 = recipes of category

        RVlayoutCustom = (RecyclerView) findViewById(R.id.RVlayoutCustom);
        RVlayoutCustom.setLayoutManager(new LinearLayoutManager(this));
        layoutTV = (TextView) findViewById(R.id.layoutTV);

        if (type.equals("1")) {
            layoutTV.setVisibility(View.VISIBLE);
        } else if (type.equals("2")) {

        }

        RVrecipes adapterRecipes = new RVrecipes();
        RVlayoutCustom.setAdapter(adapterRecipes);
    }
}

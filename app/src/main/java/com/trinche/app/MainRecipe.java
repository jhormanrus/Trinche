package com.trinche.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.awesome.shorty.AwesomeToast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.trinche.app.api.ApiAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRecipe extends AppCompatActivity {

    CircularImageView recipe_userCIV;
    TextView recipe_nameTV, recipe_userTV, recipe_dateTV, recipe_descriptionTV, recipe_categoryTV, recipe_countryTV, recipe_portionTV, recipe_timeTV, recipe_starTV, recipe_starsTV;
    ImageButton recipe_starBTN, recipe_bookBTN;
    ImageView recipe_imageIV;
    Button show_recipeBTN;
    Toolbar show_recipeT;
    Integer validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        setContentView(R.layout.activity_recipe);
    }

    private void init() {
        show_recipeT = (Toolbar) findViewById(R.id.show_recipeT);
        setSupportActionBar(show_recipeT);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        final String TOKEN = sharedPreferences.getString("TOKEN", "");
        final Intent intent = getIntent();
        Call<JsonObject> call = ApiAdapter.getApiService().readdetailRecipe(TOKEN, intent.getStringExtra("id_receta"));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, final Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    recipe_imageIV = (ImageView) findViewById(R.id.recipe_imageIV);
                    recipe_userCIV = (CircularImageView) findViewById(R.id.recipe_userCIV);
                    recipe_nameTV = (TextView) findViewById(R.id.recipe_nameTV);
                    recipe_userTV = (TextView) findViewById(R.id.recipe_userTV);
                    recipe_dateTV = (TextView) findViewById(R.id.recipe_dateTV);
                    recipe_descriptionTV = (TextView) findViewById(R.id.recipe_descriptionTV);
                    recipe_categoryTV = (TextView) findViewById(R.id.recipe_categoryTV);
                    recipe_countryTV = (TextView) findViewById(R.id.recipe_countryTV);
                    recipe_portionTV = (TextView) findViewById(R.id.recipe_portionTV);
                    recipe_timeTV = (TextView) findViewById(R.id.recipe_timeTV);
                    recipe_starsTV = (TextView) findViewById(R.id.recipe_starsTV);
                    recipe_starTV = (TextView) findViewById(R.id.recipe_starTV);
                    recipe_starBTN = (ImageButton) findViewById(R.id.recipe_starBTN);
                    recipe_bookBTN = (ImageButton) findViewById(R.id.recipe_bookBTN);
                    show_recipeBTN = (Button) findViewById(R.id.show_recipeBTN);
                    Glide.with(getApplicationContext()).load("http://104.197.2.172/image/recipe/recipedown/" + intent.getStringExtra("id_receta")).apply(new RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE)).into(recipe_imageIV);
                    Glide.with(getApplicationContext()).load("http://104.197.2.172/image/user/perfildown/" + response.body().get("ID_USUARIO").getAsString()).apply(new RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE)).into(recipe_userCIV);
                    recipe_nameTV.setText(response.body().get("NOMBRE").getAsString());
                    recipe_userTV.setText("@"+response.body().get("USUARIO").getAsString());
                    recipe_dateTV.setText(response.body().get("FECHA").getAsString());
                    recipe_descriptionTV.setText(response.body().get("DESCRIPCION").getAsString());
                    recipe_categoryTV.setText(response.body().get("CATEGORIA").getAsString());
                    recipe_countryTV.setText(response.body().get("PAIS").getAsString());
                    recipe_portionTV.setText(response.body().get("PORCIONES").getAsString());
                    recipe_timeTV.setText(response.body().get("TIEMPO").getAsString()+" minutos");
                    recipe_starsTV.setText(response.body().get("PUNTUACION").getAsString());
                    show_recipeBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), ShowRecipe.class);
                            intent.putExtra("steps", response.body().get("PASOS").toString());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplication().startActivity(intent);
                        }
                    });
                    Call<JsonObject> cali = ApiAdapter.getApiService().readscoreRanking(TOKEN);
                    cali.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                if (response.body().get("message").getAsInt() == 1) {
                                    recipe_starTV.setText("-1");
                                    validator = 0;
                                } else if (response.body().get("message").getAsInt() == 0) {
                                    recipe_starTV.setText("+1");
                                    validator = 1;
                                }
                            } else {
                                AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error inesperado").show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Intent i = getApplication().getBaseContext().getPackageManager().getLaunchIntentForPackage(getApplication().getBaseContext().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error: " + t.getLocalizedMessage()).show();
                        }
                    });
                    recipe_starBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("SCORE", validator);
                            jsonObject.addProperty("ID_PUNTUACIONES", response.body().get("ID_PUNTUACIONES").getAsString());
                            Call<JsonObject> calc = ApiAdapter.getApiService().updatescoreRanking(TOKEN, jsonObject);
                            calc.enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    if (response.isSuccessful()) {
                                        if (validator == 1) {
                                            recipe_starTV.setText("-1");
                                            validator = 0;
                                        } else if (validator == 0) {
                                            recipe_starTV.setText("+1");
                                            validator = 1;
                                        }
                                    } else {
                                        AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error inesperado").show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                    Intent i = getApplication().getBaseContext().getPackageManager().getLaunchIntentForPackage(getApplication().getBaseContext().getPackageName());
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error: " + t.getLocalizedMessage()).show();
                                }
                            });
                        }
                    });
                    recipe_bookBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AwesomeToast.INSTANCE.info(getApplication(),  "Pronto ...").show();
                        }
                    });
                } else {
                    AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error inesperado").show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Intent i = getApplication().getBaseContext().getPackageManager().getLaunchIntentForPackage(getApplication().getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error: " + t.getLocalizedMessage()).show();
            }
        });
    }
}

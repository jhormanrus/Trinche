package com.trinche.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.awesome.shorty.AwesomeToast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.trinche.app.api.ApiAdapter;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OptionRecipes extends AppCompatActivity {

    ListView LVrecipeOthers;
    JsonArray other_recipes;
    ProgressBar progressBar;
    TextView layoutTV;
    String type;
    Boolean complete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_recipes);

        SharedPreferences sharedPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        final String TOKEN = sharedPreferences.getString("TOKEN", "");
        final Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        // 1 = my recipes | 2 = recipes of book | 3 = recipes of country | 4 = recipes of category
        layoutTV = (TextView) findViewById(R.id.layoutTV);
        switch (type) {
            case "1":
                layoutTV.setVisibility(View.VISIBLE);
                break;
            case "2":

                break;
            case "3":
                layoutTV.setVisibility(View.VISIBLE);
                layoutTV.setText(intent.getStringExtra("NOMBRE"));
                Call<JsonArray> call = ApiAdapter.getApiService().readotherRecipe(TOKEN, "PAI", intent.getStringExtra("ID_PAIS"));
                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if (response.isSuccessful()) {
                            other_recipes = response.body();
                            if (other_recipes.size() == 10){
                                complete = true;
                            }
                            LVrecipeOthers = (ListView) findViewById(R.id.LVrecipeOthers);
                            final CustomAdapter customAdapter = new CustomAdapter(getApplicationContext());
                            LVrecipeOthers.setAdapter(customAdapter);
                            setListViewFooter();
                            LVrecipeOthers.setOnScrollListener(new AbsListView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(AbsListView view, int scrollState) {
                                    if(scrollState == SCROLL_STATE_IDLE && LVrecipeOthers.getLastVisiblePosition() == other_recipes.size()){
                                        progressBar.setVisibility(View.VISIBLE);
                                        if (complete) {
                                            Call<JsonArray> call = ApiAdapter.getApiService().readotherRecipe(TOKEN, other_recipes.get(other_recipes.size()-1).getAsJsonObject().get("ID_RECETA").getAsString(), intent.getStringExtra("ID_PAIS"));
                                            call.enqueue(new Callback<JsonArray>() {
                                                @Override
                                                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                                                    if (response.isSuccessful()){
                                                        if (response.body().size() == 10) {
                                                            complete = true;
                                                        } else {
                                                            complete = false;
                                                        }
                                                        for (int i = 0; i < response.body().size(); i++){
                                                            other_recipes.add(response.body().get(i).getAsJsonObject());
                                                        }
                                                        customAdapter.notifyDataSetChanged();
                                                    } else {
                                                        complete = false;
                                                        AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error inesperado").show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<JsonArray> call, Throwable t) {
                                                    complete = false;
                                                    AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error: " + t.getLocalizedMessage()).show();
                                                }
                                            });
                                        }
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
                            });
                        } else {
                            AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error inesperado").show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error: " + t.getLocalizedMessage()).show();
                    }
                });
                break;
            case "4":
                layoutTV.setVisibility(View.VISIBLE);
                layoutTV.setText(intent.getStringExtra("NOMBRE"));
                Call<JsonArray> cali = ApiAdapter.getApiService().readotherRecipe(TOKEN, "CAT", intent.getStringExtra("ID_CATEGORIA"));
                cali.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if (response.isSuccessful()) {
                            other_recipes = response.body();
                            if (other_recipes.size() == 10){
                                complete = true;
                            }
                            LVrecipeOthers = (ListView) findViewById(R.id.LVrecipeOthers);
                            final CustomAdapter customAdapter = new CustomAdapter(getApplicationContext());
                            LVrecipeOthers.setAdapter(customAdapter);
                            setListViewFooter();
                            LVrecipeOthers.setOnScrollListener(new AbsListView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(AbsListView view, int scrollState) {
                                    if(scrollState == SCROLL_STATE_IDLE && LVrecipeOthers.getLastVisiblePosition() == other_recipes.size()){
                                        progressBar.setVisibility(View.VISIBLE);
                                        if (complete) {
                                            Call<JsonArray> call = ApiAdapter.getApiService().readotherRecipe(TOKEN, other_recipes.get(other_recipes.size()-1).getAsJsonObject().get("ID_RECETA").getAsString(), intent.getStringExtra("ID_CATEGORIA"));
                                            call.enqueue(new Callback<JsonArray>() {
                                                @Override
                                                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                                                    if (response.isSuccessful()){
                                                        if (response.body().size() == 10) {
                                                            complete = true;
                                                        } else {
                                                            complete = false;
                                                        }
                                                        for (int i = 0; i < response.body().size(); i++){
                                                            other_recipes.add(response.body().get(i).getAsJsonObject());
                                                        }
                                                        customAdapter.notifyDataSetChanged();
                                                    } else {
                                                        complete = false;
                                                        AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error inesperado").show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<JsonArray> call, Throwable t) {
                                                    complete = false;
                                                    AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error: " + t.getLocalizedMessage()).show();
                                                }
                                            });
                                        }
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
                            });
                        } else {
                            AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error inesperado").show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error: " + t.getLocalizedMessage()).show();
                    }
                });
                break;
        }
    }

    class CustomAdapter extends BaseAdapter {

        Context context;

        public CustomAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return other_recipes.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView  = getLayoutInflater().inflate(R.layout.tiny_recipresentation, null);
            TextView name_recipeTV = (TextView) convertView.findViewById(R.id.name_recipeTV);
            name_recipeTV.setText(other_recipes.get(position).getAsJsonObject().get("NOMBRE").getAsString());
            TextView user_recipeTV = (TextView) convertView.findViewById(R.id.user_recipeTV);
            System.out.println(other_recipes.get(position).getAsJsonObject());
            user_recipeTV.setText("@"+other_recipes.get(position).getAsJsonObject().get("USUARIO").getAsString());
            TextView description_recipeTV = (TextView) convertView.findViewById(R.id.description_recipeTV);
            description_recipeTV.setText(other_recipes.get(position).getAsJsonObject().get("DESCRIPCION").getAsString());
            TextView date_recipeTV = (TextView) convertView.findViewById(R.id.date_recipeTV);
            try {
                date_recipeTV.setText(new SimpleDateFormat("dd/MM/yyyy").format(other_recipes.get(position).getAsJsonObject().get("FECHA").getAsString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ImageView image_recipeIV = (ImageView) convertView.findViewById(R.id.image_recipeIV);
            Glide.with(getApplicationContext()).load("http://104.197.2.172:8760/recipe/recipedown/" + other_recipes.get(position).getAsJsonObject().get("ID_RECETA").getAsString()).apply(new RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE)).into(image_recipeIV);
            CardView show_recipeCV = (CardView) convertView.findViewById(R.id.show_recipeCV);
            show_recipeCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MainRecipe.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id_receta", other_recipes.get(position).getAsJsonObject().get("ID_RECETA").getAsString());
                    context.startActivity(intent);
                }
            });
            return convertView;
        }
    }

    private void setListViewFooter(){
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tiny_progressbar, null);
        progressBar = view.findViewById(R.id.progressBar);
        LVrecipeOthers.addFooterView(progressBar);
    }
}

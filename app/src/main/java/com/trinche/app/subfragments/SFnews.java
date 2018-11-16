package com.trinche.app.subfragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.trinche.app.MainRecipe;
import com.trinche.app.R;
import com.trinche.app.api.ApiAdapter;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SFnews extends Fragment {

    ListView LVrecipeNews;
    ProgressBar progressBar;
    JsonArray recipes;
    Boolean complete = false;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.subfrag_news, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        final String TOKEN = sharedPreferences.getString("TOKEN", "");

        Call<JsonArray> call = ApiAdapter.getApiService().readallRecipe(TOKEN, null);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    recipes = response.body();
                    if (recipes.size() == 10){
                        complete = true;
                    }
                    LVrecipeNews = (ListView) v.findViewById(R.id.LVrecipeNews);
                    final CustomAdapter customAdapter = new CustomAdapter(getContext());
                    LVrecipeNews.setAdapter(customAdapter);
                    setListViewFooter();
                    LVrecipeNews.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                            if(scrollState == SCROLL_STATE_IDLE && LVrecipeNews.getLastVisiblePosition() == recipes.size()){
                                progressBar.setVisibility(View.VISIBLE);
                                if (complete) {
                                    Call<JsonArray> call = ApiAdapter.getApiService().readallRecipe(TOKEN, recipes.get(recipes.size()-1).getAsJsonObject().get("ID_RECETA").getAsString());
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
                                                    recipes.add(response.body().get(i).getAsJsonObject());
                                                }
                                                customAdapter.notifyDataSetChanged();
                                            } else {
                                                complete = false;
                                                AwesomeToast.INSTANCE.error(getActivity(),  "Error inesperado").show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JsonArray> call, Throwable t) {
                                            complete = false;
                                            Intent i = getActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(i);
                                            AwesomeToast.INSTANCE.error(getActivity(),  "Error: " + t.getLocalizedMessage()).show();
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
                    AwesomeToast.INSTANCE.error(getActivity(),  "Error inesperado").show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Intent i = getActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                AwesomeToast.INSTANCE.error(getActivity(),  "Error: " + t.getLocalizedMessage()).show();
            }
        });
        return v;
    }

    class CustomAdapter extends BaseAdapter {

        Context context;

        public CustomAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return recipes.size();
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
            name_recipeTV.setText(recipes.get(position).getAsJsonObject().get("NOMBRE").getAsString());
            TextView user_recipeTV = (TextView) convertView.findViewById(R.id.user_recipeTV);
            System.out.println(recipes.get(position).getAsJsonObject());
            user_recipeTV.setText("@"+recipes.get(position).getAsJsonObject().get("USUARIO").getAsString());
            TextView description_recipeTV = (TextView) convertView.findViewById(R.id.description_recipeTV);
            description_recipeTV.setText(recipes.get(position).getAsJsonObject().get("DESCRIPCION").getAsString());
            TextView date_recipeTV = (TextView) convertView.findViewById(R.id.date_recipeTV);
            try {
                date_recipeTV.setText(new SimpleDateFormat("dd/MM/yyyy").format(recipes.get(position).getAsJsonObject().get("FECHA").getAsString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            TextView stars_recipeTV = (TextView) convertView.findViewById(R.id.stars_recipeTV);
            stars_recipeTV.setText(recipes.get(position).getAsJsonObject().get("PUNTUACION").getAsString());
            ImageView image_recipeIV = (ImageView) convertView.findViewById(R.id.image_recipeIV);
            Glide.with(getActivity()).load("http://104.197.2.172/image/recipe/recipedown/" + recipes.get(position).getAsJsonObject().get("ID_RECETA").getAsString()).apply(new RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE)).into(image_recipeIV);
            CardView show_recipeCV = (CardView) convertView.findViewById(R.id.show_recipeCV);
            show_recipeCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MainRecipe.class);
                    intent.putExtra("id_receta", recipes.get(position).getAsJsonObject().get("ID_RECETA").getAsString());
                    context.startActivity(intent);
                }
            });
            return convertView;
        }
    }

    private void setListViewFooter(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.tiny_progressbar, null);
        progressBar = view.findViewById(R.id.progressBar);
        LVrecipeNews.addFooterView(progressBar);
    }
}
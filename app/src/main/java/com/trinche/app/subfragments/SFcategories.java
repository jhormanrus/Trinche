package com.trinche.app.subfragments;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.awesome.shorty.AwesomeToast;
import com.google.gson.JsonArray;
import com.trinche.app.OptionRecipes;
import com.trinche.app.R;
import com.trinche.app.api.ApiAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SFcategories extends Fragment {

    GridView categoriesGV;
    TextView name_categoryTV;
    ImageView image_categoryIV;
    JsonArray categories = new JsonArray();

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.subfrag_categories, container, false);

        Call<JsonArray> call = ApiAdapter.getApiService().readallCategory();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    categories = response.body();
                    categoriesGV = (GridView) v.findViewById(R.id.categoriesGV);
                    CustomAdapter customAdapter = new CustomAdapter(getActivity());
                    categoriesGV.setAdapter(customAdapter);
                } else {
                    AwesomeToast.INSTANCE.error(getContext(),  "Error inesperado").show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Intent i = getActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                AwesomeToast.INSTANCE.error(getContext(),  "Error: " + t.getLocalizedMessage()).show();
            }
        });
        return v;
    }

    class CustomAdapter extends BaseAdapter {

        Context context;
        CardView categorieCV;

        public CustomAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 22;
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
            convertView  = getLayoutInflater().inflate(R.layout.tiny_gridcategory, null);
            name_categoryTV = (TextView) convertView.findViewById(R.id.name_categoryTV);
            name_categoryTV.setText(categories.get(position).getAsJsonObject().get("NOMBRE").getAsString());
            image_categoryIV = (ImageView) convertView.findViewById(R.id.image_categoryIV);
            //GlideApp.with(SFcountries.this).load(Uri.parse("http://104.197.2.172:8760/country/image/" + countries.get(position).getAsJsonObject().get("IMAGEN").getAsString())).into(image_countryIV);
            categorieCV = (CardView) convertView.findViewById(R.id.categorieCV);
            categorieCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OptionRecipes.class);
                    intent.putExtra("type", "4");
                    intent.putExtra("ID_CATEGORIA", categories.get(position).getAsJsonObject().get("ID_CATEGORIA").getAsString());
                    intent.putExtra("NOMBRE", categories.get(position).getAsJsonObject().get("NOMBRE").getAsString());
                    context.startActivity(intent);
                }
            });
            return convertView;
        }
    }
}
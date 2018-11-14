package com.trinche.app.subfragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.trinche.app.adapters.GlideApp;
import com.trinche.app.api.ApiAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SFcountries extends Fragment {

    GridView countriesGV;
    JsonArray countries = new JsonArray();

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.subfrag_countries, container, false);

        Call<JsonArray> call = ApiAdapter.getApiService().readallCountry();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    countries = response.body();
                    countriesGV = (GridView) v.findViewById(R.id.countriesGV);
                    CustomAdapter customAdapter = new CustomAdapter(getActivity());
                    countriesGV.setAdapter(customAdapter);
                } else {
                    AwesomeToast.INSTANCE.error(getContext(),  "Error inesperado").show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                AwesomeToast.INSTANCE.error(getContext(),  "Error: " + t.getLocalizedMessage()).show();
            }
        });

        return v;
    }

    class CustomAdapter extends BaseAdapter {

        Context context;
        CardView countrieCV;

        public CustomAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 21;
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
            convertView  = getLayoutInflater().inflate(R.layout.tiny_gridcountrie, null);
            TextView name_countryTV = (TextView) convertView.findViewById(R.id.name_countryTV);
            name_countryTV.setText(countries.get(position).getAsJsonObject().get("NOMBRE").getAsString());
            ImageView image_countryIV = (ImageView) convertView.findViewById(R.id.image_countryTV);
            GlideApp.with(SFcountries.this).load(Uri.parse("http://104.197.2.172:8760/country/image/" + countries.get(position).getAsJsonObject().get("IMAGEN").getAsString())).into(image_countryIV);
            countrieCV = (CardView) convertView.findViewById(R.id.countrieCV);
            countrieCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OptionRecipes.class);
                    intent.putExtra("type", "3");
                    intent.putExtra("ID_PAIS", countries.get(position).getAsJsonObject().get("ID_PAIS").getAsString());
                    intent.putExtra("NOMBRE", countries.get(position).getAsJsonObject().get("NOMBRE").getAsString());
                    context.startActivity(intent);
                }
            });
            return convertView;
        }
    }
}

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
import android.widget.Button;
import android.widget.GridView;

import com.trinche.app.OptionRecipes;
import com.trinche.app.R;

public class SFcountries extends Fragment {

    GridView countriesGV;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.subfrag_countries, container, false);

        countriesGV = (GridView) v.findViewById(R.id.countriesGV);
        CustomAdapter customAdapter = new CustomAdapter(getActivity());
        countriesGV.setAdapter(customAdapter);
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
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView  = getLayoutInflater().inflate(R.layout.tiny_gridcountrie, null);
            countrieCV = (CardView) convertView.findViewById(R.id.countrieCV);
            countrieCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OptionRecipes.class);
                    intent.putExtra("type", "3");
                    context.startActivity(intent);
                }
            });
            return convertView;
        }
    }
}

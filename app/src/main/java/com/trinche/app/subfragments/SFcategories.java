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

import com.trinche.app.OptionRecipes;
import com.trinche.app.R;

public class SFcategories extends Fragment {

    GridView categoriesGV;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.subfrag_categories, container, false);

        categoriesGV = (GridView) v.findViewById(R.id.categoriesGV);
        CustomAdapter customAdapter = new CustomAdapter(getActivity());
        categoriesGV.setAdapter(customAdapter);
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
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView  = getLayoutInflater().inflate(R.layout.tiny_gridcategory, null);
            categorieCV = (CardView) convertView.findViewById(R.id.categorieCV);
            categorieCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OptionRecipes.class);
                    intent.putExtra("type", "4");
                    context.startActivity(intent);
                }
            });
            return convertView;
        }
    }
}

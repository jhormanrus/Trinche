package com.trinche.app.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trinche.app.R;
import com.trinche.app.adapters.RVranking;

public class LOranking extends Fragment {

    RecyclerView RVrecipeRanking;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_ranking, container, false);

        RVrecipeRanking = (RecyclerView) v.findViewById(R.id.RVrecipeRanking);
        RVrecipeRanking.setLayoutManager(new LinearLayoutManager(getContext()));

        RVranking adapterRecipes = new RVranking();
        RVrecipeRanking.setAdapter(adapterRecipes);

        return v;
    }
}

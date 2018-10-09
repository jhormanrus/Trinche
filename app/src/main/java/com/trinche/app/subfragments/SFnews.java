package com.trinche.app.subfragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trinche.app.adapters.RVrecipes;
import com.trinche.app.R;

public class SFnews extends Fragment {

    RecyclerView RVrecipeNews;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.subfrag_news, container, false);

        RVrecipeNews = (RecyclerView) v.findViewById(R.id.RVrecipeNews);
        RVrecipeNews.setLayoutManager(new LinearLayoutManager(getContext()));

        RVrecipes adapterRecipes = new RVrecipes();
        RVrecipeNews.setAdapter(adapterRecipes);

        return v;
    }
}

package com.trinche.app.adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.trinche.app.R;
import com.trinche.app.objects.Usuario;

public class RVresponseAdapter extends SuggestionsAdapter<Usuario, RVresponseHolder> {

    public RVresponseAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    @Override
    public void onBindSuggestionHolder(Usuario suggestion, RVresponseHolder holder, int position) {
        /*holder.nameFD.setText(suggestion.get("NAME").toString());
        holder.tagFD.setText(suggestion.get("TAG").toString());
        holder.emailFD.setText(suggestion.get("EMAIL").toString());*/
    }

    @Override
    public int getSingleViewHeight() {
        return 100;
    }

    @NonNull
    @Override
    public RVresponseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = getLayoutInflater().inflate(R.layout.tiny_finduser, viewGroup, false);
        return new RVresponseHolder(view);
    }
}

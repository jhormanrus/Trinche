package com.trinche.app.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trinche.app.R;

public class RVrecipes extends RecyclerView.Adapter<RVrecipes.ViewHolderRecipes> {

    @NonNull
    @Override
    public ViewHolderRecipes onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tiny_recipresentation, null, false);
        return new ViewHolderRecipes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderRecipes viewHolderRecipes, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolderRecipes extends RecyclerView.ViewHolder {
        public ViewHolderRecipes(@NonNull View itemView) {
            super(itemView);
        }
    }
}

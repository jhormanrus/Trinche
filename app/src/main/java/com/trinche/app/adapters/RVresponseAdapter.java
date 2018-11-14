package com.trinche.app.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.google.gson.JsonObject;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.trinche.app.R;
import com.trinche.app.objects.Usuario;

import java.util.ArrayList;
import java.util.List;

public class RVresponseAdapter extends SuggestionsAdapter<JsonObject, RVresponseHolder> {

    public RVresponseAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    @Override
    public void onBindSuggestionHolder(JsonObject suggestion, RVresponseHolder holder, final int position) {
        holder.ingredientFD.setText(suggestion.get("NOMBRE").toString().replace("\"", ""));
        holder.ingredientCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(position);
            }
        });
    }

    @Override
    public int getSingleViewHeight() {
        return 50;
    }

    @NonNull
    @Override
    public RVresponseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = getLayoutInflater().inflate(R.layout.tiny_finingredient, viewGroup, false);
        return new RVresponseHolder(view);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String term = constraint.toString();
                if(term.isEmpty())
                    suggestions = suggestions_clone;
                else {
                    suggestions = new ArrayList<>();
                    for (JsonObject item: suggestions_clone)
                        if(item.get("NOMBRE").toString().toLowerCase().contains(term.toLowerCase()))
                            suggestions.add(item);
                }
                results.values = suggestions;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                suggestions = (ArrayList<JsonObject>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}

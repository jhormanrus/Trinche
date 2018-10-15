package com.trinche.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trinche.app.OptionRecipes;
import com.trinche.app.R;

public class RVbooks extends RecyclerView.Adapter<RVbooks.ViewHolderBooks> {

    private Context context;

    public RVbooks(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderBooks onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tiny_bookpresentation, null, false);
        return new ViewHolderBooks(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderBooks viewHolderBooks, final int i) {
        viewHolderBooks.bookCV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OptionRecipes.class);
                intent.putExtra("type", "2");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolderBooks extends RecyclerView.ViewHolder {
        CardView bookCV;

        public ViewHolderBooks(@NonNull View itemView) {
            super(itemView);

            bookCV = (CardView) itemView.findViewById(R.id.bookCV);
        }
    }
}

package com.trinche.app.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.trinche.app.R;

public class RVresponseHolder extends RecyclerView.ViewHolder {

    protected TextView ingredientFD;
    protected CardView ingredientCV;

    public RVresponseHolder(@NonNull View itemView) {
        super(itemView);
        ingredientFD = (TextView) itemView.findViewById(R.id.ingredientFD);
        ingredientCV = (CardView) itemView.findViewById(R.id.ingredientCV);

    }
}

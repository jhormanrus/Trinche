package com.trinche.app.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.trinche.app.R;

public class RVresponseHolder extends RecyclerView.ViewHolder {

    protected CircularImageView profileFD;
    protected TextView nameFD, tagFD, emailFD;

    public RVresponseHolder(@NonNull View itemView) {
        super(itemView);
        profileFD = (CircularImageView) itemView.findViewById(R.id.profileFD);
        nameFD = (TextView) itemView.findViewById(R.id.nameFD);
        tagFD = (TextView) itemView.findViewById(R.id.tagFD);
        emailFD = (TextView) itemView.findViewById(R.id.emailFD);
    }
}

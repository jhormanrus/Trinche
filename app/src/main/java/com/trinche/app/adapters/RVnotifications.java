package com.trinche.app.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trinche.app.R;

public class RVnotifications extends RecyclerView.Adapter<RVnotifications.ViewHolderNotifications> {

    @NonNull
    @Override
    public RVnotifications.ViewHolderNotifications onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tiny_notifications, null, false);
        return new RVnotifications.ViewHolderNotifications(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVnotifications.ViewHolderNotifications viewHolderNotifications, int i) {

    }

    @Override
    public int getItemCount()  {
        return 10;
    }
    public class ViewHolderNotifications extends RecyclerView.ViewHolder {
        public ViewHolderNotifications(@NonNull View itemView) {
            super(itemView);
        }
    }
}

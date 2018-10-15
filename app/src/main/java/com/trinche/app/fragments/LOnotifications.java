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
import com.trinche.app.adapters.RVnotifications;

public class LOnotifications extends Fragment {

    RecyclerView RVNotificacion;
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_notifications, container, false);

        RVNotificacion = (RecyclerView) v.findViewById(R.id.RVNotificacion);
        RVNotificacion.setLayoutManager(new LinearLayoutManager(getContext()));

        RVnotifications adapterNotifications = new RVnotifications();
        RVNotificacion.setAdapter(adapterNotifications);
        return v;
    }
}

package com.trinche.app.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

<<<<<<< HEAD:app/src/main/java/com/trinche/app/fragments/LOnotifications.java
import com.trinche.app.R;
=======
import com.trinche.trinch.R;
import com.trinche.trinch.adapters.RVNotifications;
>>>>>>> f99232de286b61048930002b0fe4dc7a37d5ce7a:app/src/main/java/com/trinche/trinch/fragments/LOnotifications.java

public class LOnotifications extends Fragment {

    RecyclerView RVNotificacion;
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_rv_notif, container, false);

        RVNotificacion = (RecyclerView) v.findViewById(R.id.RVNotificacion);
        RVNotificacion.setLayoutManager(new LinearLayoutManager(getContext()));

        RVNotifications adapterNotifications = new RVNotifications();
        RVNotificacion.setAdapter(adapterNotifications);
        return v;
    }
}

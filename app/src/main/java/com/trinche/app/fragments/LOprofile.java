package com.trinche.app.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.awesome.dialog.AwesomeCustomDialog;
import com.trinche.app.MainSetting;
import com.trinche.app.OptionBooks;
import com.trinche.app.OptionRecipes;
import com.trinche.app.R;

import org.jetbrains.annotations.NotNull;

import at.markushi.ui.CircleButton;

public class LOprofile extends Fragment implements View.OnClickListener {

    CircleButton show_recipeCBT, show_bookCBT, show_followingCBT, show_followersCBT, show_settingCBT;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_profile, container, false);

        init(v);
        handler.post(runnable);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_recipeCBT:
                Intent intent1 = new Intent(getActivity(), OptionRecipes.class);
                intent1.putExtra("type", "1");
                startActivity(intent1);
                break;

            case R.id.show_bookCBT:
                Intent intent2 = new Intent(getActivity(), OptionBooks.class);
                startActivity(intent2);
                break;

            case R.id.show_followersCBT:
                new AwesomeCustomDialog(v.getContext()).setTopColor(Color.parseColor("#FFB475")).setIcon(R.mipmap.twotone_supervisor_account_white_36dp)
                        .setTitle("Seguidores").setView(R.layout.tiny_follow).configureView(new AwesomeCustomDialog.ViewConfigurator() {
                    @Override
                    public void configureView(@NotNull View view) {
                        ListView followLV = (ListView) view.findViewById(R.id.follorLV);
                        CustomAdapter customAdapter = new CustomAdapter(1);
                        followLV.setAdapter(customAdapter);
                    }
                }).show();
                break;

            case R.id.show_followingCBT:
                new AwesomeCustomDialog(v.getContext()).setTopColor(Color.parseColor("#FFB475")).setIcon(R.mipmap.twotone_people_white_36dp)
                        .setTitle("Seguidos").setView(R.layout.tiny_follow).configureView(new AwesomeCustomDialog.ViewConfigurator() {
                    @Override
                    public void configureView(@NotNull View view) {
                        ListView followLV = (ListView) view.findViewById(R.id.follorLV);
                        CustomAdapter customAdapter = new CustomAdapter(2);
                        followLV.setAdapter(customAdapter);
                    }
                }).show();
                break;

            case R.id.show_settingCBT:
                Intent intent3 = new Intent(getActivity(), MainSetting.class);
                startActivity(intent3);
                break;
        }
    }

    private void init(View v) {
        show_recipeCBT = (CircleButton) v.findViewById(R.id.show_recipeCBT);
        show_recipeCBT.setOnClickListener(this);
        show_bookCBT = (CircleButton) v.findViewById(R.id.show_bookCBT);
        show_bookCBT.setOnClickListener(this);
        show_followingCBT = (CircleButton) v.findViewById(R.id.show_followingCBT);
        show_followingCBT.setOnClickListener(this);
        show_followersCBT = (CircleButton) v.findViewById(R.id.show_followersCBT);
        show_followersCBT.setOnClickListener(this);
        show_settingCBT = (CircleButton) v.findViewById(R.id.show_settingCBT);
        show_settingCBT.setOnClickListener(this);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            show_followersCBT.setVisibility(View.VISIBLE);
            show_followingCBT.setVisibility(View.VISIBLE);
            show_settingCBT.setVisibility(View.VISIBLE);
        }
    };

    class CustomAdapter extends BaseAdapter {

        int type;
        // 1 = followers | 2 = following

        CustomAdapter(int type) {
            this.type = type;
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView  = getLayoutInflater().inflate(R.layout.tiny_follow_child, null);
            if (type == 2) {
                Button followBTN = (Button) convertView.findViewById(R.id.followBTN);
                followBTN.setText("No seguir");
                followBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
            return convertView;
        }
    }
}

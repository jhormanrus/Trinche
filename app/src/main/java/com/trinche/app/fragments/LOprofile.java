package com.trinche.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.awesome.dialog.AwesomeCustomDialog;
import com.awesome.shorty.AwesomeToast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.trinche.app.MainSetting;
import com.trinche.app.OptionBooks;
import com.trinche.app.OptionRecipes;
import com.trinche.app.R;
import com.trinche.app.api.ApiAdapter;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import at.markushi.ui.CircleButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LOprofile extends Fragment implements View.OnClickListener {

    LinearLayout show_recipeLL, show_bookLL, show_followingLL, show_followersLL, show_settingLL;
    CircleButton show_recipeCBT, show_bookCBT, show_followingCBT, show_followersCBT, show_settingCBT;
    CircularImageView perfilCIV;
    TextView read_nomapTV, read_userTV, read_descriptionTV;
    ImageView portadaIV;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_profile, container, false);

        init(v);
        handler.post(runnable);
        charge();
        return v;
    }

    private void charge() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        final String ID_USUARIO = sharedPreferences.getString("ID_USUARIO", "");
        final String TOKEN = sharedPreferences.getString("TOKEN", "");

        Glide.get(getActivity()).clearMemory();
        Glide.with(this).load( "http://104.197.2.172:8760/user/perfildown/" + ID_USUARIO).apply(new RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE)).into(perfilCIV);
        Glide.with(this).load("http://104.197.2.172:8760/user/portadadown/" + ID_USUARIO).apply(new RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE)).into(portadaIV);

        Call<JsonObject> firstcall = ApiAdapter.getApiService().readUsuario(TOKEN);
        firstcall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject result = response.body().getAsJsonObject();
                    read_nomapTV.setText(result.get("NOM_AP").getAsString());
                    read_userTV.setText("@" + result.get("USUARIO").getAsString());
                    try {
                        read_descriptionTV.setText(result.get("DESCRIPCION").getAsString());
                    } catch (Exception e) {
                        read_descriptionTV.setText("");
                    }
                } else {
                    AwesomeToast.INSTANCE.error(getContext(),  "Error inesperado").show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AwesomeToast.INSTANCE.error(getContext(),  "Error: " + t.getLocalizedMessage()).show();
            }
        });
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
                        ListView followLV = (ListView) view.findViewById(R.id.followLV);
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
                        ListView followLV = (ListView) view.findViewById(R.id.followLV);
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
        show_recipeLL = (LinearLayout) v.findViewById(R.id.show_recipeLL);
        show_bookLL = (LinearLayout) v.findViewById(R.id.show_bookLL);
        show_followingLL = (LinearLayout) v.findViewById(R.id.show_followingLL);
        show_followersLL = (LinearLayout) v.findViewById(R.id.show_followersLL);
        show_settingLL = (LinearLayout) v.findViewById(R.id.show_settingLL);
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
        perfilCIV = (CircularImageView) v.findViewById(R.id.perfilCIV);
        portadaIV = (ImageView) v.findViewById(R.id.portadaIV);
        read_nomapTV = (TextView) v.findViewById(R.id.read_nomapTV);
        read_userTV = (TextView) v.findViewById(R.id.read_userTV);
        read_descriptionTV = (TextView) v.findViewById(R.id.read_descriptionTV);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            show_followersLL.setVisibility(View.VISIBLE);
            show_followingLL.setVisibility(View.VISIBLE);
            show_settingLL.setVisibility(View.VISIBLE);
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

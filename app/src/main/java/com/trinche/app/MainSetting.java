package com.trinche.app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.awesome.dialog.AwesomeCustomDialog;
import com.awesome.dialog.AwesomeGeneralDialog;
import com.awesome.dialog.AwesomeProgressDialog;
import com.awesome.shorty.AwesomeToast;
import com.google.gson.JsonObject;
import com.trinche.app.api.ApiAdapter;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Fragment fragment = new SettingsScreen();
        FragmentTransaction transaction= getFragmentManager().beginTransaction();
        transaction.add(R.id.configuration_fragmentRELAY, fragment);
        transaction.commit();
    }

    public static class SettingsScreen extends PreferenceFragment implements Preference.OnPreferenceClickListener {

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_profile);

            Preference update_profile = (Preference) getPreferenceScreen().findPreference("update_profilePFR");
            update_profile.setOnPreferenceClickListener(this);
            Preference close_session = (Preference) getPreferenceScreen().findPreference("close_sessionPFR");
            close_session.setOnPreferenceClickListener(this);
            Preference delete_account = (Preference) getPreferenceScreen().findPreference("delete_accountPFR");
            delete_account.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
            final String TOKEN = sharedPreferences.getString("TOKEN", "");
            switch (preference.getKey()) {
                case "update_profilePFR":
                    Intent intent = new Intent(getActivity(), SettingProfile.class);
                    startActivity(intent);
                    return true;

                case "close_sessionPFR":
                    new AwesomeGeneralDialog(getActivity()).setTopColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                            .setIcon(R.drawable.ic_twotone_account_circle_48px).setIconTintColor(Color.parseColor("#FFFFFF")).setTitle("Cerrar sesión")
                            .setPositiveButton("Si", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
                                    sharedPreferences.edit().clear().apply();
                                    Intent i = getActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    AwesomeToast.INSTANCE.success(getActivity(), "Se ha cerrado sesión").show();
                                }
                            }).setNegativeButton("No", null).setMessage("Estas a punto de cerrar sesión, ¿Proceder?")
                                .setPositiveButtonColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                                .show();
                    return true;

                case "delete_accountPFR":
                    new AwesomeCustomDialog(getActivity()).setTopColor(Color.parseColor("#FFB475")).setIcon(R.drawable.ic_twotone_delete_48px).setIconTintColor(Color.parseColor("#FFFFFF"))
                            .setTitle("Eliminar cuenta").setMessage("Estas a punto de eliminar tu cuenta, valida tu contraseña para continuar")
                            .setView(R.layout.tiny_dialogdeleteaccount).configureView(new AwesomeCustomDialog.ViewConfigurator() {
                        @Override
                        public void configureView(@NotNull View view) {
                            final EditText delete_userET = (EditText) view.findViewById(R.id.delete_userET);
                            Button borrarBTN = (Button) view.findViewById(R.id.borrarBTN);
                            borrarBTN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Dialog progress = new AwesomeProgressDialog(getActivity()).setIcon(R.drawable.ic_twotone_vpn_key_48px).setTopColor(Color.parseColor("#FFFFFF"))
                                            .setTitle("Eliminando").setTopColorRes(R.color.colorPrimary).show();
                                    Handler handler = new Handler();
                                    Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            final JsonObject jsonObject = new JsonObject();
                                            jsonObject.addProperty("CONTRASENA", delete_userET.getText().toString());
                                            Call<JsonObject> call = ApiAdapter.getApiService().deleteUsuario(TOKEN, jsonObject);
                                            call.enqueue(new Callback<JsonObject>() {
                                                @Override
                                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                                    if (response.isSuccessful()) {
                                                        if (response.body().get("message").getAsString().equals("1")) {
                                                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
                                                            sharedPreferences.edit().clear().apply();
                                                            progress.dismiss();
                                                            Intent i = getActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(i);
                                                            AwesomeToast.INSTANCE.success(getActivity(), "Se ha eliminado su cuenta").show();
                                                        } else if (response.body().get("message").getAsString().equals("105")) {
                                                            progress.dismiss();
                                                            AwesomeToast.INSTANCE.warning(getActivity(),  "Usuario inválido").show();
                                                        } else if (response.body().get("message").getAsString().equals("106")) {
                                                            progress.dismiss();
                                                            AwesomeToast.INSTANCE.warning(getActivity(),  "Contraseña incorrecta").show();
                                                        }
                                                    } else {
                                                        AwesomeToast.INSTANCE.error(getActivity(),  "Error inesperado").show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                                    Intent i = getActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(i);
                                                    AwesomeToast.INSTANCE.error(getActivity(),  "Error: " + t.getLocalizedMessage()).show();
                                                }
                                            });
                                        }
                                    };
                                    handler.postDelayed(runnable, 2000);
                                }
                            });
                        }
                    }).show();
                    return true;

                default:
                    return false;
            }
        }
    }
}

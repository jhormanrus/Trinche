package com.trinche.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.awesome.shorty.AwesomeToast;
import com.google.gson.JsonObject;
import com.trinche.app.api.ApiAdapter;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainLogin extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout Login, Signup;
    EditText Username, Password;
    Button Register;
    CircularProgressButton Enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        handler.postDelayed(runnable, 2000);
        validate();
    }

    private void validate() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        String TOKEN = sharedPreferences.getString("TOKEN", "");
        if (!TOKEN.equals("")){
            Call<JsonObject> call = ApiAdapter.getApiService().validateUsuario(TOKEN);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getAsJsonObject().get("message").toString().equals("1")) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error: " + t.getLocalizedMessage()).show();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enterBTN:
                Enter.startAnimation();
                AUTENTICAR();
                break;

            case R.id.signupBTN:
                Intent intent = new Intent(MainLogin.this, MainRegister.class);
                startActivity(intent);
                break;
        }
    }

    private void AUTENTICAR () {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("USUARIO", Username.getText().toString());
        jsonObject.addProperty("CONTRASENA", Password.getText().toString());
        Call<JsonObject> call = ApiAdapter.getApiService().loginUsuario(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JsonObject result = response.body().getAsJsonObject();
                        PREFERENCES(result.get("ID_USUARIO").getAsString(), result.get("TOKEN").getAsString());
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        AwesomeToast.INSTANCE.success(getApplicationContext(), "Logeado correctamente").show();
                    } catch (Exception e) {
                        System.out.println(e);
                        if (response.body().getAsJsonObject().get("message").toString().equals("105")) {
                            AwesomeToast.INSTANCE.warning(getApplicationContext(),  "Usuario inválido").show();
                        } else if (response.body().getAsJsonObject().get("message").toString().equals("106")) {
                            AwesomeToast.INSTANCE.warning(getApplicationContext(),  "Contraseña inválida").show();
                        }
                    }
                } else {
                    AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error inesperado").show();
                }
                Enter.revertAnimation();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error: " + t.getLocalizedMessage()).show();
                Enter.revertAnimation();
            }
        });
    }

    private void PREFERENCES (String ID_USUARIO, String TOKEN) {
        SharedPreferences sharedPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ID_USUARIO", ID_USUARIO);
        editor.putString("TOKEN", TOKEN);
        editor.apply();
    }

    private void init() {
        Login = (RelativeLayout) findViewById(R.id.relayLogin);
        Signup = (RelativeLayout) findViewById(R.id.relaySignup);
        Username = (EditText) findViewById(R.id.usernameET);
        Password = (EditText) findViewById(R.id.passwordET);
        Enter = (CircularProgressButton) findViewById(R.id.enterBTN);
        Enter.setOnClickListener(this);
        Register = (Button) findViewById(R.id.signupBTN);
        Register.setOnClickListener(this);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Login.setVisibility(View.VISIBLE);
            Signup.setVisibility(View.VISIBLE);
        }
    };
}

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
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.tfb.fbtoast.FBToast;
import com.trinche.app.api.ApiAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainLogin extends AppCompatActivity {

    RelativeLayout Login, Signup;
    EditText Username, Password;
    Button Enter, Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        handler.postDelayed(runnable, 2000);

        SharedPreferences sharedPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        if (!sharedPreferences.getString("username", "").equals("") && !sharedPreferences.getString("password", "").equals("")){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AUTENTICAR(Username.getText().toString(), Password.getText().toString());
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainLogin.this, MainRegister.class);
                startActivity(intent);
            }
        });
    }

    private void AUTENTICAR (String username, String password) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("USUARIO", Username.getText().toString());
        jsonObject.addProperty("CONTRASENA", Password.getText().toString());
        Call<JsonObject> call = ApiAdapter.getApiService("8000").loginUsuario(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        PREFERENCES(response.body().getAsJsonObject().get("TOKEN").toString());
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        FBToast.successToast(getApplicationContext(),"Logeado correctamente",FBToast.LENGTH_SHORT);
                    } catch (Exception e) {
                        System.out.println(e);
                        if (response.body().getAsJsonObject().get("message").toString().equals("105")) {
                            FBToast.errorToast(getApplicationContext(),"Usuario inválido",FBToast.LENGTH_SHORT);
                        } else if (response.body().getAsJsonObject().get("message").toString().equals("106")) {
                            FBToast.errorToast(getApplicationContext(),"Contraseña inválida",FBToast.LENGTH_SHORT);
                        }
                    }
                } else {
                    FBToast.errorToast(getApplicationContext(),"Error inesperado",FBToast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                FBToast.errorToast(getApplicationContext(),"Error: " + t.getLocalizedMessage(),FBToast.LENGTH_SHORT);
            }
        });
    }

    private void PREFERENCES (String TOKEN) {
        SharedPreferences sharedPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TOKEN", TOKEN);
        editor.apply();
    }

    private void init() {
        Login = (RelativeLayout) findViewById(R.id.relayLogin);
        Signup = (RelativeLayout) findViewById(R.id.relaySignup);
        Username = (EditText) findViewById(R.id.usernameET);
        Password = (EditText) findViewById(R.id.passwordET);
        Enter = (Button) findViewById(R.id.enterBTN);
        Register = (Button) findViewById(R.id.signupBTN);
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

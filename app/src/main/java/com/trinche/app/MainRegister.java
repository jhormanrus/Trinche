package com.trinche.app;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.awesome.shorty.AwesomeToast;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.raywenderlich.android.validatetor.ValidateTor;
import com.trinche.app.api.ApiAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import am.appwise.components.ni.NoInternetDialog;
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRegister extends AppCompatActivity implements View.OnClickListener {

    Button create_fechaBTN, signinBTN;
    EditText create_usuarioET, create_contrasenaET, create_nom_apET, create_correoET;
    Spinner create_genderSN, create_countrySN;
    CircularProgressButton registerCPB;
    ValidateTor validateTor = new ValidateTor();
    NoInternetDialog noInternetDialog;
    ArrayList<String> countries = new ArrayList<>();
    ArrayList<String> id_countries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        noInternetDialog = new NoInternetDialog.Builder(this).setBgGradientStart(1).setBgGradientCenter(1).setBgGradientEnd(1).setCancelable(true).build();

        Call<JsonArray> call = ApiAdapter.getApiService().readallCountry();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    JsonArray result = response.body().getAsJsonArray();
                    for (int i = 0; i < result.size(); i++) {
                        countries.add(result.get(i).getAsJsonObject().get("NOMBRE").getAsString());
                        id_countries.add(result.get(i).getAsJsonObject().get("ID_PAIS").getAsString());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, countries);
                    adapter.setDropDownViewResource(R.layout.spinner_selected);
                    create_countrySN.setAdapter(adapter);
                } else {
                    AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error inesperado").show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error: " + t.getLocalizedMessage()).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_fechaBTN:
                Calendar c = Calendar.getInstance();
                int dia = c.get(Calendar.DAY_OF_MONTH);
                int mes = c.get(Calendar.MONTH);
                int ano = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String CERO = "0";
                        String BARRA = "/";
                        final int mesActual = month + 1;
                        String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                        String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                        create_fechaBTN.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                    }
                }, ano, mes, dia);
                datePickerDialog.show();
                break;

            case R.id.registerCPB:
                registerCPB.startAnimation();
                if (validateTor.isAtMostLength(create_usuarioET.getText().toString(), 20)){
                    if (validateTor.isAlphanumeric(create_usuarioET.getText().toString())) {
                        if (validateTor.isAtMostLength(create_contrasenaET.getText().toString(), 20)) {
                            if (validateTor.isAlphanumeric(create_contrasenaET.getText().toString())) {
                                if (validateTor.isAtMostLength(create_nom_apET.getText().toString(), 50)) {
                                    if (!create_fechaBTN.getText().toString().equals("")){
                                        if (validateTor.isAtMostLength(create_correoET.getText().toString(), 120)){
                                            if (validateTor.isEmail(create_correoET.getText().toString())){
                                                final JsonObject jsonObject = new JsonObject();
                                                jsonObject.addProperty("USUARIO", create_usuarioET.getText().toString());
                                                jsonObject.addProperty("CONTRASENA", create_contrasenaET.getText().toString());
                                                jsonObject.addProperty("NOM_AP", create_nom_apET.getText().toString());
                                                jsonObject.addProperty("CORREO", create_correoET.getText().toString());
                                                jsonObject.addProperty("FECHA_NAC", create_fechaBTN.getText().toString());
                                                jsonObject.addProperty("GENERO", create_genderSN.getSelectedItemPosition());
                                                jsonObject.addProperty("ID_PAIS", id_countries.get(create_countrySN.getSelectedItemPosition()));
                                                Call<JsonObject> call = ApiAdapter.getApiService().createUsuario(jsonObject);
                                                call.enqueue(new Callback<JsonObject>() {
                                                    @Override
                                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                                        if (response.isSuccessful()){
                                                            switch (response.body().getAsJsonObject().get("message").toString()) {
                                                                case "1":
                                                                    onBackPressed();
                                                                    AwesomeToast.INSTANCE.success(getApplicationContext(), "Usuario registrado").show();
                                                                    break;
                                                                case "0":
                                                                    AwesomeToast.INSTANCE.warning(getApplicationContext(), "Este usuario ya existe").show();
                                                                    break;
                                                                case "2":
                                                                    AwesomeToast.INSTANCE.warning(getApplicationContext(), "Este correo ya existe").show();
                                                                    break;
                                                            }
                                                        } else {
                                                            AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error inesperado").show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<JsonObject> call, Throwable t) {
                                                        AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error: " + t.getLocalizedMessage()).show();
                                                    }
                                                });
                                            } else {
                                                AwesomeToast.INSTANCE.warning(getApplicationContext(),  "Correo inválido").show();
                                            }
                                        } else {
                                            AwesomeToast.INSTANCE.warning(getApplicationContext(),  "CORREO - Máximo 120 caracteres").show();
                                        }
                                    } else {
                                        AwesomeToast.INSTANCE.warning(getApplicationContext(),  "Escoge tu fecha de nacimiento").show();
                                    }
                                } else {
                                    AwesomeToast.INSTANCE.warning(getApplicationContext(),  "NOMBRES - Máximo 50 caracteres").show();
                                }
                            } else {
                                AwesomeToast.INSTANCE.warning(getApplicationContext(),  "CONTRASEÑA - Solo letras y números").show();
                            }
                        } else {
                            AwesomeToast.INSTANCE.warning(getApplicationContext(),  "CONTRASEÑA - Máximo 20 caracteres").show();
                        }
                    } else {
                        AwesomeToast.INSTANCE.warning(getApplicationContext(),  "USUARIO - Solo letras y números").show();
                    }
                } else {
                    AwesomeToast.INSTANCE.warning(getApplicationContext(),  "USUARIO - Máximo 20 caracteres").show();
                }
                registerCPB.revertAnimation();
                break;

            case R.id.signinBTN:
                onBackPressed();
                break;
        }
    }

    private void init() {
        create_fechaBTN = (Button) findViewById(R.id.create_fechaBTN);
        create_fechaBTN.setOnClickListener(this);
        signinBTN = (Button) findViewById(R.id.signinBTN);
        signinBTN.setOnClickListener(this);
        registerCPB = (CircularProgressButton) findViewById(R.id.registerCPB);
        registerCPB.setOnClickListener(this);
        create_usuarioET = (EditText) findViewById(R.id.create_usuarioET);
        create_contrasenaET = (EditText) findViewById(R.id.create_contrasenaET);
        create_nom_apET = (EditText) findViewById(R.id.create_nom_apeET);
        create_correoET = (EditText) findViewById(R.id.create_correoET);
        create_genderSN = (Spinner) findViewById(R.id.create_genderSN);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.spinner_gender));
        adapter.setDropDownViewResource(R.layout.spinner_selected);
        create_genderSN.setAdapter(adapter);
        create_countrySN = (Spinner) findViewById(R.id.create_countrySN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }
}
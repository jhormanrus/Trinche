package com.trinche.app;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.JsonObject;
import com.raywenderlich.android.validatetor.ValidateTor;
import com.tfb.fbtoast.FBToast;
import com.trinche.app.api.ApiAdapter;

import java.util.Calendar;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRegister extends AppCompatActivity implements View.OnClickListener {

    Button create_fechaBTN, registerBTN, signinBTN;
    EditText create_usuarioET, create_contrasenaET, create_nom_apET, create_paisET, create_correoET;
    Spinner create_genderSN;
    ValidateTor validateTor = new ValidateTor();
    //NoInternetDialog noInternetDialog = new NoInternetDialog.Builder(getApplicationContext()).build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
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

            case R.id.registerBTN:
                if (validateTor.isEmail(create_correoET.getText().toString())){
                    final JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("USUARIO", create_usuarioET.getText().toString());
                    jsonObject.addProperty("CONTRASENA", create_contrasenaET.getText().toString());
                    jsonObject.addProperty("NOM_AP", create_nom_apET.getText().toString());
                    jsonObject.addProperty("CORREO", create_correoET.getText().toString());
                    jsonObject.addProperty("FECHA_NAC", create_fechaBTN.getText().toString());
                    jsonObject.addProperty("GENERO", create_genderSN.getSelectedItemPosition());
                    jsonObject.addProperty("ID_PAIS", create_paisET.getText().toString());
                    Call<JsonObject> call = ApiAdapter.getApiService("8000").createUsuario(jsonObject);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()){
                                onBackPressed();
                                FBToast.successToast(getApplicationContext(),"Usuario registrado",FBToast.LENGTH_SHORT);
                            } else {
                                FBToast.errorToast(getApplicationContext(),"Error inesperado",FBToast.LENGTH_SHORT);
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            FBToast.errorToast(getApplicationContext(),"Error: " + t.getLocalizedMessage(),FBToast.LENGTH_SHORT);
                        }
                    });
                } else {
                    FBToast.errorToast(getApplicationContext(),"Correo inválido",FBToast.LENGTH_SHORT);
                }
                break;

            case R.id.signinBTN:
                onBackPressed();
                break;
        }
    }

    private void init() {
        create_fechaBTN = (Button) findViewById(R.id.create_fechaBTN);
        create_fechaBTN.setOnClickListener(this);
        registerBTN = (Button) findViewById(R.id.registerBTN);
        registerBTN.setOnClickListener(this);
        signinBTN = (Button) findViewById(R.id.signinBTN);
        signinBTN.setOnClickListener(this);
        create_usuarioET = (EditText) findViewById(R.id.create_usuarioET);
        create_contrasenaET = (EditText) findViewById(R.id.create_contrasenaET);
        create_nom_apET = (EditText) findViewById(R.id.create_nom_apeET);
        create_paisET = (EditText) findViewById(R.id.create_paisET);
        create_correoET = (EditText) findViewById(R.id.create_correoET);
        create_genderSN = (Spinner) findViewById(R.id.create_genderSN);
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }*/
}

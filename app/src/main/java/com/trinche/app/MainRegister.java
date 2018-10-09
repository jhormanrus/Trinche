package com.trinche.app;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.raywenderlich.android.validatetor.ValidateTor;
import com.tfb.fbtoast.FBToast;

import java.util.Calendar;

import am.appwise.components.ni.NoInternetDialog;

public class MainRegister extends AppCompatActivity implements View.OnClickListener {

    Button create_fechaBTN, registerBTN;
    EditText create_correoET;
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
                    FBToast.successToast(getApplicationContext(),"Nice",FBToast.LENGTH_SHORT);
                } else {
                    FBToast.errorToast(getApplicationContext(),"Correo inválido",FBToast.LENGTH_SHORT);
                }
                break;
        }
    }

    private void init() {
        create_fechaBTN = (Button) findViewById(R.id.create_fechaBTN);
        create_fechaBTN.setOnClickListener(this);
        registerBTN = (Button) findViewById(R.id.registerBTN);
        registerBTN.setOnClickListener(this);
        create_correoET = (EditText) findViewById(R.id.create_correoET);
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }*/
}

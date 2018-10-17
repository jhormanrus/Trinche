package com.trinche.app;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.awesome.dialog.AwesomeCustomDialog;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class SettingProfile extends AppCompatActivity implements View.OnClickListener {

    Button update_contrasenaBTN, update_fechaBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);

        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_contrasenaBTN:
                new AwesomeCustomDialog(this).setTopColor(Color.parseColor("#FFB475")).setIcon(R.drawable.ic_twotone_vpn_key_48px).setIconTintColor(Color.parseColor("#FFFFFF"))
                        .setTitle("Cambiar contraseña").setMessage("Para cambiar tu contraseña, llena los siguientes datos")
                        .setView(R.layout.tiny_dialogupdatepassword).configureView(new AwesomeCustomDialog.ViewConfigurator() {
                    @Override
                    public void configureView(@NotNull View view) {

                    }
                }).show();
                break;

            case R.id.update_fechaBTN:
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
                        String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                        String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                        update_fechaBTN.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                    }
                }, ano, mes, dia);
                datePickerDialog.show();
                break;
        }
    }

    private void init() {
        update_contrasenaBTN = (Button) findViewById(R.id.update_contrasenaBTN);
        update_contrasenaBTN.setOnClickListener(this);
        update_fechaBTN = (Button) findViewById(R.id.update_fechaBTN);
        update_fechaBTN.setOnClickListener(this);
    }
}

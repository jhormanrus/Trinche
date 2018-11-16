package com.trinche.app;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.awesome.dialog.AwesomeCustomDialog;
import com.awesome.shorty.AwesomeToast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.raywenderlich.android.validatetor.ValidateTor;
import com.trinche.app.api.ApiAdapter;
import com.trinche.app.api.ImageAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingProfile extends AppCompatActivity implements View.OnClickListener {

    Button update_contrasenaBTN, update_fechaBTN, contrasenaBTN;
    ImageButton update_imageBTN, update_portadaBTN;
    ImageView update_portadaIV;
    EditText update_usuarioET, update_nomapET, update_descriptionET, update_correoET, update_actualcontrasenaET, update_newcontrasenaET, update_repeatcontrasenaET;
    CircularImageView update_imageCIV;
    Spinner update_genderSN, update_countrySN;
    CircularProgressButton updateCPB;
    ArrayList<String> countries = new ArrayList<>();
    ArrayList<String> id_countries = new ArrayList<>();
    Uri fileUri1, fileUri2;
    ValidateTor validateTor = new ValidateTor();
    Integer value1 = 0, value2 = 0, type = 0;

    final int REQUEST_CODE_GALLERY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);

        init();

        charge();
    }

    private void charge() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        final String ID_USUARIO = sharedPreferences.getString("ID_USUARIO", "");
        final String TOKEN = sharedPreferences.getString("TOKEN", "");

        Glide.get(this).clearMemory();
        Glide.with(this).load("http://104.197.2.172/image/user/perfildown/" + ID_USUARIO).apply(new RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE)).into(update_imageCIV);
        Glide.with(this).load("http://104.197.2.172/image/user/portadadown/" + ID_USUARIO).apply(new RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE)).into(update_portadaIV);

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
                    update_countrySN.setAdapter(adapter);

                    Call<JsonObject> firstcall = ApiAdapter.getApiService().readUsuario(TOKEN);
                    firstcall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                JsonObject result = response.body().getAsJsonObject();
                                update_usuarioET.setText(result.get("USUARIO").getAsString());
                                update_nomapET.setText(result.get("NOM_AP").toString().replace("\"", ""));
                                try {
                                    update_descriptionET.setText(result.get("DESCRIPCION").getAsString());
                                } catch (Exception e) {
                                    update_descriptionET.setText("");
                                }
                                update_fechaBTN.setText(result.get("FECHA_NAC").getAsString());
                                update_genderSN.setSelection(result.get("GENERO").getAsInt());
                                update_countrySN.setSelection(id_countries.indexOf(result.get("ID_PAIS").getAsString()));
                                update_correoET.setText(result.get("CORREO").getAsString());
                            } else {
                                AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error inesperado").show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Intent i = getApplication().getBaseContext().getPackageManager().getLaunchIntentForPackage(getApplication().getBaseContext().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error: " + t.getLocalizedMessage()).show();
                        }
                    });
                } else {
                    AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error inesperado").show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Intent i = getApplication().getBaseContext().getPackageManager().getLaunchIntentForPackage(getApplication().getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error: " + t.getLocalizedMessage()).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        final String ID_USUARIO = sharedPreferences.getString("ID_USUARIO", "");
        final String TOKEN = sharedPreferences.getString("TOKEN", "");

        switch (v.getId()) {
            case R.id.update_contrasenaBTN:
                new AwesomeCustomDialog(this).setTopColor(Color.parseColor("#FFB475")).setIcon(R.drawable.ic_twotone_vpn_key_48px).setIconTintColor(Color.parseColor("#FFFFFF"))
                        .setTitle("Cambiar contraseña").setMessage("Para cambiar tu contraseña, llena los siguientes datos")
                        .setView(R.layout.tiny_dialogupdatepassword).configureView(new AwesomeCustomDialog.ViewConfigurator() {
                    @Override
                    public void configureView(@NotNull View view) {
                        update_actualcontrasenaET = (EditText) view.findViewById(R.id.update_actualcontrasenaET);
                        update_newcontrasenaET = (EditText) view.findViewById(R.id.update_newcontrasenaET);
                        update_repeatcontrasenaET = (EditText) view.findViewById(R.id.update_repeatcontrasenaET);
                        contrasenaBTN = (Button) view.findViewById(R.id.contrasenaBTN);
                        contrasenaBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (validateTor.isAtMostLength(update_newcontrasenaET.getText().toString(), 20)) {
                                    if (validateTor.isAlphanumeric(update_newcontrasenaET.getText().toString())) {
                                        if (validateTor.containsSubstring(update_repeatcontrasenaET.getText().toString(), update_newcontrasenaET.getText().toString())) {
                                            final JsonObject jsonObject = new JsonObject();
                                            jsonObject.addProperty("CONTRASENA", update_actualcontrasenaET.getText().toString());
                                            jsonObject.addProperty("NEWCONTRASENA", update_newcontrasenaET.getText().toString());
                                            Call<JsonObject> call = ApiAdapter.getApiService().passwordUsuario(TOKEN, jsonObject);
                                            call.enqueue(new Callback<JsonObject>() {
                                                @Override
                                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                                    if (response.isSuccessful()) {
                                                        switch (response.body().getAsJsonObject().get("message").toString()) {
                                                            case "1":
                                                                onBackPressed();
                                                                AwesomeToast.INSTANCE.success(getApplicationContext(), "Contraseña actualizada").show();
                                                                break;
                                                            case "235":
                                                                AwesomeToast.INSTANCE.warning(getApplicationContext(), "La contraseña no puede ser la misma que la actual").show();
                                                                break;
                                                            case "236":
                                                                AwesomeToast.INSTANCE.warning(getApplicationContext(), "Contraseña inválida").show();
                                                                break;
                                                        }
                                                    } else {
                                                        AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error inesperado").show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                                    Intent i = getApplication().getBaseContext().getPackageManager().getLaunchIntentForPackage(getApplication().getBaseContext().getPackageName());
                                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(i);
                                                    AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error: " + t.getLocalizedMessage()).show();
                                                }
                                            });
                                        } else {
                                            AwesomeToast.INSTANCE.warning(getApplicationContext(),  "REPETIR NUEVA CONTRASEÑA - Contraseña incorrecta").show();
                                        }
                                    } else {
                                        AwesomeToast.INSTANCE.warning(getApplicationContext(),  "NUEVA CONTRASEÑA - Solo letras y números").show();
                                    }
                                } else {
                                    AwesomeToast.INSTANCE.warning(getApplicationContext(),  "NUEVA CONTRASEÑA - Máximo 20 caracteres").show();
                                }
                            }
                        });
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

            case R.id.update_imageBTN:
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
                break;

            case R.id.update_portadaBTN:
                type = 1;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
                break;

            case R.id.updateCPB:
                updateCPB.startAnimation();
                if (validateTor.isAtMostLength(update_usuarioET.getText().toString(), 20)){
                    if (validateTor.isAlphanumeric(update_usuarioET.getText().toString())) {
                        if (validateTor.isAtMostLength(update_nomapET.getText().toString(), 50)) {
                            if (validateTor.isAtMostLength(update_descriptionET.getText().toString(), 300)) {
                                if (!update_fechaBTN.getText().toString().equals("")){
                                    if (validateTor.isAtMostLength(update_correoET.getText().toString(), 120)){
                                        if (validateTor.isEmail(update_correoET.getText().toString())){
                                            if (value1 == 1) {
                                                byte[] imagen = imageViewtoByte(update_imageCIV);
                                                RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), (imagen));
                                                MultipartBody.Part file = MultipartBody.Part.createFormData("file", getFileName(fileUri1), body);
                                                Call<JsonObject> call = ImageAdapter.getApiService().uploadUsuario(file, ID_USUARIO);
                                                call.enqueue(new Callback<JsonObject>() {
                                                    @Override
                                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                                        if (!response.isSuccessful()) {
                                                            AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error inesperado al actualizar imagen de perfil").show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<JsonObject> call, Throwable t) {
                                                        Intent i = getApplication().getBaseContext().getPackageManager().getLaunchIntentForPackage(getApplication().getBaseContext().getPackageName());
                                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(i);
                                                        AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error: " + t.getLocalizedMessage()).show();
                                                    }
                                                });
                                            }
                                            if (value2 == 1) {
                                                byte[] imagen = imageViewtoByte(update_portadaIV);
                                                RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), (imagen));
                                                MultipartBody.Part file = MultipartBody.Part.createFormData("file", getFileName(fileUri2), body);
                                                Call<JsonObject> call = ImageAdapter.getApiService().portadaupUsuario(file, ID_USUARIO);
                                                call.enqueue(new Callback<JsonObject>() {
                                                    @Override
                                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                                        if (!response.isSuccessful()) {
                                                            AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error inesperado al actualizar imagen de portada").show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<JsonObject> call, Throwable t) {
                                                        Intent i = getApplication().getBaseContext().getPackageManager().getLaunchIntentForPackage(getApplication().getBaseContext().getPackageName());
                                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(i);
                                                        AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error: " + t.getLocalizedMessage()).show();
                                                    }
                                                });
                                            }
                                            final JsonObject jsonObject = new JsonObject();
                                            jsonObject.addProperty("USUARIO", update_usuarioET.getText().toString());
                                            jsonObject.addProperty("NOM_AP", update_nomapET.getText().toString());
                                            jsonObject.addProperty("DESCRIPCION", update_descriptionET.getText().toString());
                                            jsonObject.addProperty("CORREO", update_correoET.getText().toString());
                                            jsonObject.addProperty("FECHA_NAC", update_fechaBTN.getText().toString());
                                            jsonObject.addProperty("GENERO", update_genderSN.getSelectedItemPosition());
                                            jsonObject.addProperty("ID_PAIS", id_countries.get(update_countrySN.getSelectedItemPosition()));
                                            Call<JsonObject> cali = ApiAdapter.getApiService().updateUsuario(TOKEN, jsonObject);
                                            cali.enqueue(new Callback<JsonObject>() {
                                                @Override
                                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                                    if (response.isSuccessful()) {
                                                        switch (response.body().getAsJsonObject().get("message").toString()) {
                                                            case "1":
                                                                onBackPressed();
                                                                AwesomeToast.INSTANCE.success(getApplicationContext(), "Usuario actualizado").show();
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
                                                    Intent i = getApplication().getBaseContext().getPackageManager().getLaunchIntentForPackage(getApplication().getBaseContext().getPackageName());
                                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(i);
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
                                AwesomeToast.INSTANCE.warning(getApplicationContext(),  "DESCRIPCIÓN - Máximo 300 caracteres").show();
                            }
                        } else {
                            AwesomeToast.INSTANCE.warning(getApplicationContext(),  "NOMBRES - Máximo 50 caracteres").show();
                        }
                    } else {
                        AwesomeToast.INSTANCE.warning(getApplicationContext(),  "USUARIO - Solo letras y números").show();
                    }
                } else {
                    AwesomeToast.INSTANCE.warning(getApplicationContext(),  "USUARIO - Máximo 20 caracteres").show();
                }
                updateCPB.revertAnimation();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                AwesomeToast.INSTANCE.warning(getApplicationContext(),  "La aplicación no tiene permisos para acceder a la galeria").show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri fileUri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(fileUri);
                File actualImage = FileUtil.from(this, data.getData());
                if (type == 0) {
                    Bitmap compressed = new Compressor(this).setMaxWidth(180).setMaxHeight(180).setDestinationDirectoryPath(this.getFilesDir().getAbsolutePath()).compressToBitmap(actualImage);
                    update_imageCIV.setImageBitmap(compressed);
                    fileUri1 = fileUri;
                    value1 = 1;
                } else if (type == 1) {
                    Bitmap compressed = new Compressor(this).setMaxWidth(851).setMaxHeight(315).setDestinationDirectoryPath(this.getFilesDir().getAbsolutePath()).compressToBitmap(actualImage);
                    update_portadaIV.setImageBitmap(compressed);
                    fileUri2 = fileUri;
                    value2 = 1;
                    type = 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private byte[] imageViewtoByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void init() {
        update_contrasenaBTN = (Button) findViewById(R.id.update_contrasenaBTN);
        update_contrasenaBTN.setOnClickListener(this);
        update_fechaBTN = (Button) findViewById(R.id.update_fechaBTN);
        update_fechaBTN.setOnClickListener(this);
        update_imageBTN = (ImageButton) findViewById(R.id.update_imageBTN);
        update_imageBTN.setOnClickListener(this);
        update_portadaBTN = (ImageButton) findViewById(R.id.update_portadaBTN);
        update_portadaBTN.setOnClickListener(this);
        update_portadaIV = (ImageView) findViewById(R.id.update_portadaIV);
        update_usuarioET = (EditText) findViewById(R.id.update_usuarioET);
        update_nomapET = (EditText) findViewById(R.id.update_nomapeET);
        update_descriptionET = (EditText) findViewById(R.id.update_desciptionET);
        update_correoET = (EditText) findViewById(R.id.update_correoET);
        update_imageCIV = (CircularImageView) findViewById(R.id.update_imageCIV);
        update_genderSN = (Spinner) findViewById(R.id.update_genderSN);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.spinner_gender));
        adapter.setDropDownViewResource(R.layout.spinner_selected);
        update_genderSN.setAdapter(adapter);
        update_countrySN = (Spinner) findViewById(R.id.update_countrySN);
        updateCPB = (CircularProgressButton) findViewById(R.id.updateCPB);
        updateCPB.setOnClickListener(this);
    }
}

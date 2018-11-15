package com.trinche.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.awesome.shorty.AwesomeToast;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.trinche.app.adapters.MNGrecipe;
import com.trinche.app.adapters.STEPcreate_recipe;
import com.trinche.app.api.ApiAdapter;
import com.trinche.app.api.ImageAdapter;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OptionCreateRecipe extends AppCompatActivity implements StepperLayout.StepperListener, MNGrecipe {

    StepperLayout stepper_create;
    JsonObject recipe;
    JsonArray steps;
    byte[] imageRecipe;
    Uri urlRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_createrecipe);

        init();
    }

    private void init() {
        stepper_create = (StepperLayout) findViewById(R.id.stepper_create);
        stepper_create.setAdapter(new STEPcreate_recipe(getSupportFragmentManager(), this));
    }

    @Override
    public void onCompleted(View completeButton) {
        System.out.println("completed");
    }

    @Override
    public void onError(VerificationError verificationError) {
        System.out.println("Error: " + verificationError.getErrorMessage());
    }

    @Override
    public void onStepSelected(int newStepPosition) {
        System.out.println("Step: " + newStepPosition);
    }

    @Override
    public void onReturn() {
        System.out.println("return");
    }

    @Override
    public void saveReceta(JsonObject jsonObject) {
        recipe = jsonObject;
    }

    @Override
    public void savePasos(final JsonArray jsonArray, final List<byte[]> imByte, final List<Uri> imUri) {
        SharedPreferences sharedPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        final String TOKEN = sharedPreferences.getString("TOKEN", "");

        steps = jsonArray;
        recipe.add("PASOS", steps);
        Call<JsonObject> call = ApiAdapter.getApiService().createReceta(TOKEN, recipe);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    AwesomeToast.INSTANCE.success(getApplicationContext(),  "Receta creada correctamente").show();
                    JsonObject respuesta = response.body();
                    RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), (imageRecipe));
                    MultipartBody.Part file = MultipartBody.Part.createFormData("file", getFileName(urlRecipe), body);
                    String ID_RECETA = respuesta.get("ID_RECETA").getAsString();
                    Call<JsonObject> cali = ImageAdapter.getApiService().uploadRecipe(file, ID_RECETA);
                    cali.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (!response.isSuccessful()) {
                                AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error inesperado al subir la imagen de tu receta").show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error: " + t.getLocalizedMessage()).show();
                        }
                    });
                    for (int i = 0; i < jsonArray.size(); i++) {
                        RequestBody boddy = RequestBody.create(MediaType.parse("image/jpeg"), (imByte.get(i)));
                        MultipartBody.Part fail = MultipartBody.Part.createFormData("file", getFileName(imUri.get(i)), boddy);
                        String ID_PASOS = respuesta.get("PASOS").getAsJsonArray().get(i).getAsJsonObject().get("ID_PASOS").getAsString();
                        Call<JsonObject> calc = ImageAdapter.getApiService().uploadStep(fail, ID_PASOS);
                        calc.clone().enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (!response.isSuccessful()) {
                                    AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error inesperado al subir la imagen de los pasos de tu receta").show();
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                AwesomeToast.INSTANCE.error(getApplicationContext(),  "Error: " + t.getLocalizedMessage()).show();
                            }
                        });
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

    @Override
    public void saveRecetaImage(byte[] file, Uri url) {
        imageRecipe = file;
        urlRecipe = url;
    }
}

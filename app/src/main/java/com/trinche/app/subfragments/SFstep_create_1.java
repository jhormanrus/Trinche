package com.trinche.app.subfragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.awesome.shorty.AwesomeToast;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.raywenderlich.android.validatetor.ValidateTor;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.trinche.app.R;
import com.trinche.app.adapters.MNGrecipe;
import com.trinche.app.api.ApiAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class SFstep_create_1 extends Fragment implements BlockingStep, View.OnClickListener {

    EditText create_nom_reET, create_descriptionET, create_timeET, create_portionsET;
    Spinner re_categorySN, re_countrySN;
    Button create_imageBTN;
    ImageView create_imageIV;
    ValidateTor validateTor = new ValidateTor();
    ArrayList<String> countries = new ArrayList<>();
    ArrayList<String> id_countries = new ArrayList<>();
    ArrayList<String> categories = new ArrayList<>();
    ArrayList<String> id_categories = new ArrayList<>();
    final int REQUEST_CODE_GALLERY = 999;
    Uri fileUrf;
    Integer value = 0;

    private MNGrecipe mnGrecipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.subfrag_step_create_1, container, false);

        init(v);
        charge();
        return v;
    }

    private void charge() {
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, countries);
                    adapter.setDropDownViewResource(R.layout.spinner_selected);
                    re_countrySN.setAdapter(adapter);
                } else {
                    AwesomeToast.INSTANCE.error(getContext(),  "Error inesperado").show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                AwesomeToast.INSTANCE.error(getContext(),  "Error: " + t.getLocalizedMessage()).show();
            }
        });

        Call<JsonArray> cali = ApiAdapter.getApiService().readallCategory();
        cali.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    JsonArray result = response.body().getAsJsonArray();
                    for (int i = 0; i < result.size(); i++) {
                        categories.add(result.get(i).getAsJsonObject().get("NOMBRE").getAsString());
                        id_categories.add(result.get(i).getAsJsonObject().get("ID_CATEGORIA").getAsString());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, categories);
                    adapter.setDropDownViewResource(R.layout.spinner_selected);
                    re_categorySN.setAdapter(adapter);
                } else {
                    AwesomeToast.INSTANCE.error(getContext(),  "Error inesperado").show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                AwesomeToast.INSTANCE.error(getContext(),  "Error: " + t.getLocalizedMessage()).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_imageBTN:
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
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
                AwesomeToast.INSTANCE.warning(getActivity().getApplicationContext(),  "La aplicación no tiene permisos para acceder a la galeria").show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri fileUri = data.getData();

            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(fileUri);
                File actualImage = FileUtil.from(getContext(), data.getData());
                Bitmap compressed = new Compressor(getContext()).setMaxWidth(180).setMaxHeight(180).setDestinationDirectoryPath(getActivity().getFilesDir().getAbsolutePath()).compressToBitmap(actualImage);
                create_imageIV.setImageBitmap(compressed);
                fileUrf = fileUri;
                value = 1;
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

    private void init(View view) {
        create_nom_reET = (EditText) view.findViewById(R.id.create_nom_reET);
        create_descriptionET = (EditText) view.findViewById(R.id.create_descriptionET);
        create_timeET = (EditText) view.findViewById(R.id.create_timeET);
        create_portionsET = (EditText) view.findViewById(R.id.create_portionsET);
        re_categorySN = (Spinner) view.findViewById(R.id.re_categorySN);
        re_countrySN = (Spinner) view.findViewById(R.id.re_countrySN);
        create_imageBTN = (Button) view.findViewById(R.id.create_imageBTN);
        create_imageBTN.setOnClickListener(this);
        create_imageIV = (ImageView) view.findViewById(R.id.create_imageIV);
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        if (value == 1) {
            if (!create_nom_reET.getText().toString().equals("")) {
                if (validateTor.isAtMostLength(create_nom_reET.getText().toString(), 50)){
                    if (!create_descriptionET.getText().toString().equals("")) {
                        if (validateTor.isAtMostLength(create_descriptionET.getText().toString(), 500)) {
                            if (validateTor.isNumeric(create_timeET.getText().toString())) {
                                if (validateTor.isAtMostLength(create_timeET.getText().toString(), 10)) {
                                    if (validateTor.isNumeric(create_portionsET.getText().toString())) {
                                        if (validateTor.isAtMostLength(create_portionsET.getText().toString(), 10)) {
                                            final JsonObject jsonObject = new JsonObject();
                                            jsonObject.addProperty("NOMBRE", create_nom_reET.getText().toString());
                                            jsonObject.addProperty("DESCRIPCION", create_descriptionET.getText().toString());
                                            jsonObject.addProperty("PORCIONES", create_portionsET.getText().toString());
                                            jsonObject.addProperty("ID_PAIS", id_countries.get(re_countrySN.getSelectedItemPosition()));
                                            jsonObject.addProperty("ID_CATEGORIA", id_categories.get(re_categorySN.getSelectedItemPosition()));
                                            jsonObject.addProperty("TIEMPO", create_timeET.getText().toString());
                                            mnGrecipe.saveReceta(jsonObject);
                                            byte[] imagen = imageViewtoByte(create_imageIV);
                                            mnGrecipe.saveRecetaImage(imagen, fileUrf);
                                            callback.goToNextStep();
                                        } else {
                                            AwesomeToast.INSTANCE.warning(getContext(),  "PORCIONES - Máximo 10 caracteres").show();
                                        }
                                    } else {
                                        AwesomeToast.INSTANCE.warning(getContext(),  "PORCIONES - Solo números enteros").show();
                                    }
                                } else {
                                    AwesomeToast.INSTANCE.warning(getContext(),  "TIEMPO - Máximo 10 caracteres").show();
                                }
                            } else {
                                AwesomeToast.INSTANCE.warning(getContext(),  "TIEMPO - Solo números enteros").show();
                            }
                        } else {
                            AwesomeToast.INSTANCE.warning(getContext(),  "DESCRIPCIÓN - Máximo 500 caracteres").show();
                        }
                    } else {
                        AwesomeToast.INSTANCE.warning(getContext(),  "DESCRIPCIÓN - El campo está vacío").show();
                    }
                } else {
                    AwesomeToast.INSTANCE.warning(getContext(),  "NOMBRE DE RECETA - Máximo 50 caracteres").show();
                }
            } else {
                AwesomeToast.INSTANCE.warning(getContext(),  "NOMBRE DE RECETA - El campo está vacío").show();
            }
        } else {
            AwesomeToast.INSTANCE.warning(getContext(),  "Suba una imagen para su receta").show();
        }
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        System.out.println("complete");
        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        System.out.println("back");
        getActivity().finish();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {
        System.out.println("Error");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MNGrecipe) {
            mnGrecipe = (MNGrecipe) context;
        } else {
            throw new IllegalStateException("Activity must implement DataManager interface!");
        }
    }
}

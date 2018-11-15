package com.trinche.app.subfragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.awesome.dialog.AwesomeCustomDialog;
import com.awesome.shorty.AwesomeToast;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.raywenderlich.android.validatetor.ValidateTor;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.trinche.app.R;
import com.trinche.app.adapters.MNGrecipe;
import com.trinche.app.api.ApiAdapter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class SFstep_create_2 extends Fragment implements BlockingStep, View.OnClickListener {

    ListView stepsLV, ingredientsLV;
    FloatingActionButton add_stepFAB;
    ValidateTor validateTor = new ValidateTor();
    List<JsonObject> listSteps = new ArrayList<JsonObject>();
    List<JsonObject> listIngredients = new ArrayList<JsonObject>();
    JsonArray imIngredients;                              //
    List<byte[]>  imByte = new ArrayList<byte[]>();       // ----> to send
    List<Uri> imUri = new ArrayList<Uri>();               //
    JsonObject STEP;
    List<JsonObject> ingredients = new ArrayList<JsonObject>();
    CustomAdapter adapter;
    MaterialSearchBar search_ingredientMSB;
    SFstep_create_2.CustomAdapter customAdapter;
    ImageView create_stepIV;
    Button create_stepBTN;
    Spinner medida_ingredientSN;
    final int REQUEST_CODE_GALLERY = 999;
    Uri fileUrf;
    Integer value = 0;

    private MNGrecipe mnGrecipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.subfrag_step_create_2, container, false);

        init(v);
        return v;
    }

    private void init(View view) {
        stepsLV = (ListView) view.findViewById(R.id.stepsLV);
        customAdapter = new SFstep_create_2.CustomAdapter(listSteps, 1);
        stepsLV.setAdapter(customAdapter);
        add_stepFAB = (FloatingActionButton) view.findViewById(R.id.add_stepFAB);
        add_stepFAB.setOnClickListener(this);
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

        if (listSteps.size() > 0) {
            JsonArray JsonStep = new JsonArray();
            for (int i = 0; i < listSteps.size(); i++) {
                JsonObject processed = new JsonObject();
                processed = listSteps.get(i);
                processed.addProperty("N_PASO", Integer.toString(i+1));
                JsonStep.add(processed);
            }
            mnGrecipe.savePasos(JsonStep, imByte, imUri);
            getActivity().onBackPressed();
        } else {
            AwesomeToast.INSTANCE.warning(getContext(),  "No hay pasos suficientes para continuar").show();
        }
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_stepFAB:
                final AwesomeCustomDialog acd = new AwesomeCustomDialog(getContext());
                acd.setTopColor(Color.parseColor("#FFB475")).setIcon(R.drawable.ic_twotone_receipt_24px).setIconTintColor(Color.parseColor("#FFFFFF"))
                        .setTitle("Añadir paso").setView(R.layout.tiny_steps_add).configureView(new AwesomeCustomDialog.ViewConfigurator() {
                    @Override
                    public void configureView(@NotNull final View view) {
                        listIngredients.clear();
                        final EditText name_stepET = (EditText) view.findViewById(R.id.name_stepET);
                        final EditText description_stepET = (EditText) view.findViewById(R.id.desciption_stepET);
                        create_stepIV = (ImageView) view.findViewById(R.id.create_stepIV);
                        create_stepBTN = (Button) view.findViewById(R.id.create_stepBTN);
                        create_stepBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
                            }
                        });
                        search_ingredientMSB = (MaterialSearchBar) view.findViewById(R.id.search_ingredientMSB);
                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                        final RVresponseAdapter rVresponseAdapter = new RVresponseAdapter(inflater);

                        Call<JsonArray> call = ApiAdapter.getApiService().readallIngredient();
                        call.enqueue(new Callback<JsonArray>() {
                            @Override
                            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                                if (response.isSuccessful()) {
                                    JsonArray result = response.body().getAsJsonArray();
                                    for (int i = 0; i < result.size(); i++) {
                                        ingredients.add(result.get(i).getAsJsonObject());
                                    }
                                    rVresponseAdapter.setSuggestions(ingredients);
                                    search_ingredientMSB.setMaxSuggestionCount(2);
                                    search_ingredientMSB.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                                        @Override
                                        public void onSearchStateChanged(boolean enabled) {

                                        }

                                        @Override
                                        public void onSearchConfirmed(CharSequence text) {
                                            if (!text.toString().equals("")) {
                                                search_ingredientMSB.setCustomSuggestionAdapter(rVresponseAdapter);
                                                search_ingredientMSB.showSuggestionsList();
                                                rVresponseAdapter.getFilter().filter(search_ingredientMSB.getText());
                                            }
                                        }

                                        @Override
                                        public void onButtonClicked(int buttonCode) {}
                                    });
                                } else {
                                    AwesomeToast.INSTANCE.error(getActivity().getApplicationContext(),  "Error inesperado").show();
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonArray> call, Throwable t) {
                                AwesomeToast.INSTANCE.error(getActivity().getApplicationContext(),  "Error: " + t.getLocalizedMessage()).show();
                            }
                        });

                        ingredientsLV = (ListView) view.findViewById(R.id.ingredientsLV);
                        adapter = new CustomAdapter(listIngredients, 2);
                        ingredientsLV.setAdapter(adapter);

                        Button add_stepBTN = (Button) view.findViewById(R.id.add_stepBTN);
                        add_stepBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (value == 1) {
                                    if (!name_stepET.getText().toString().equals("")){
                                        if (validateTor.isAtMostLength(name_stepET.getText().toString(), 50)) {
                                            if (!description_stepET.getText().toString().equals("")) {
                                                if (validateTor.isAtMostLength(description_stepET.getText().toString(), 1000)){
                                                    imIngredients = new JsonArray();
                                                    View viuw;
                                                    Boolean state_peso = true;
                                                    Boolean state_length = true;
                                                    Boolean state_double = true;
                                                    for (int i = 0; i < ingredientsLV.getCount(); i++) {
                                                        viuw = ingredientsLV.getChildAt(i);
                                                        EditText peso_ingredientET = (EditText) viuw.findViewById(R.id.peso_ingredientET);
                                                        Spinner medida_ingredientSN = (Spinner) viuw.findViewById(R.id.medida_ingredientSN);
                                                        JsonObject yeison = new JsonObject();
                                                        yeison.addProperty("ID_INGREDIENTES", listIngredients.get(i).get("ID_INGREDIENTES").getAsString());
                                                        yeison.addProperty("PESO", peso_ingredientET.getText().toString().replace(",", "."));
                                                        yeison.addProperty("MEDIDA", medida_ingredientSN.getSelectedItemPosition());
                                                        imIngredients.add(yeison);
                                                        if (peso_ingredientET.getText().toString().equals("")) {
                                                            state_peso = false;
                                                        }
                                                        if (!validateTor.isAtMostLength(peso_ingredientET.getText().toString(), 10)) {
                                                            state_length = false;
                                                        }
                                                        if (!validateTor.isDecimal(peso_ingredientET.getText().toString())) {
                                                            state_double = false;
                                                        }
                                                    }
                                                    if (state_peso) {
                                                        if (state_length) {
                                                            if (state_double) {
                                                                STEP = new JsonObject();
                                                                STEP.addProperty("NOMBRE", name_stepET.getText().toString());
                                                                STEP.addProperty("DESCRIPCION", description_stepET.getText().toString());
                                                                STEP.add("DETALLE_PASOS", imIngredients);
                                                                listSteps.add(STEP);
                                                                imByte.add(imageViewtoByte(create_stepIV));
                                                                imUri.add(fileUrf);
                                                                customAdapter.notifyDataSetChanged();
                                                                acd.dismiss();
                                                                value = 0;
                                                            } else {
                                                                AwesomeToast.INSTANCE.warning(getContext(),  "Solo se acepta decimales en las medidas").show();
                                                            }
                                                        } else {
                                                            AwesomeToast.INSTANCE.warning(getContext(),  "Uno o más de las medidas excede el máximo de caracteres (10)").show();
                                                        }
                                                    } else {
                                                        AwesomeToast.INSTANCE.warning(getContext(),  "Faltan medida(s) en tu lista de ingredientes").show();
                                                    }
                                                } else {
                                                    AwesomeToast.INSTANCE.warning(getContext(),  "DESCRIPCIÓN - Máximo 1000 caracteres").show();
                                                }
                                            } else {
                                                AwesomeToast.INSTANCE.warning(getContext(),  "DESCRIPCIÓN - El campo está vacío").show();
                                            }
                                        } else {
                                            AwesomeToast.INSTANCE.warning(getContext(),  "NOMBRE DEL PASO - Máximo 50 caracteres").show();
                                        }
                                    } else {
                                        AwesomeToast.INSTANCE.warning(getContext(),  "NOMBRE DEL PASO - El campo está vacío").show();
                                    }
                                } else {
                                    AwesomeToast.INSTANCE.warning(getContext(),  "Suba una imagen para su receta").show();
                                }
                            }
                        });
                    }
                }).show();
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
                create_stepIV.setImageBitmap(compressed);
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

    class RVresponseHolder extends RecyclerView.ViewHolder {

        protected TextView ingredientFD, categoryFD;
        protected CardView ingredientCV;

        public RVresponseHolder(@NonNull View itemView) {
            super(itemView);
            ingredientFD = (TextView) itemView.findViewById(R.id.ingredientFD);
            categoryFD = (TextView) itemView.findViewById(R.id.categoryFD);
            ingredientCV = (CardView) itemView.findViewById(R.id.ingredientCV);
        }
    }

    class RVresponseAdapter extends SuggestionsAdapter<JsonObject, RVresponseHolder> {

        public RVresponseAdapter(LayoutInflater inflater) {
            super(inflater);
        }

        @Override
        public void onBindSuggestionHolder(final JsonObject suggestion, final RVresponseHolder holder, final int position) {
            holder.ingredientFD.setText(suggestion.get("NOMBRE").getAsString());
            holder.categoryFD.setText(suggestion.get("TIPO").getAsString());
            holder.ingredientCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listIngredients.add(suggestion);
                    adapter.notifyDataSetChanged();
                    search_ingredientMSB.hideSuggestionsList();
                }
            });
        }

        @Override
        public int getSingleViewHeight() {
            return 50;
        }

        @NonNull
        @Override
        public RVresponseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(R.layout.tiny_finingredient, viewGroup, false);
            return new RVresponseHolder(view);
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    String term = constraint.toString();
                    if(term.isEmpty())
                        suggestions = suggestions_clone;
                    else {
                        suggestions = new ArrayList<>();
                        for (JsonObject item: suggestions_clone)
                            if(item.get("NOMBRE").toString().toLowerCase().contains(term.toLowerCase()))
                                suggestions.add(item);
                    }
                    results.values = suggestions;
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    suggestions = (ArrayList<JsonObject>) results.values;
                    notifyDataSetChanged();
                }
            };
        }
    }

    class CustomAdapter extends BaseAdapter {

        List<JsonObject> jsonObjects;
        Integer type;

        public CustomAdapter(List<JsonObject> jsonObjects, Integer type) {
            this.jsonObjects = jsonObjects;
            this.type = type;
        }

        @Override
        public int getCount() {
            return jsonObjects.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //type 1 = step, type 2 = ingredients
            if (type == 1) {
                convertView  = getLayoutInflater().inflate(R.layout.tiny_steps_child, null);
                TextView number_stepTV = (TextView) convertView.findViewById(R.id.number_stepTV);
                number_stepTV.setText(Integer.toString(position + 1));
                TextView name_stepTV = (TextView) convertView.findViewById(R.id.name_stepTV);
                name_stepTV.setText(jsonObjects.get(position).get("NOMBRE").getAsString());
                ImageButton delete_stepBTN = (ImageButton) convertView.findViewById(R.id.delete_stepBTN);
                delete_stepBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listSteps.remove(position);
                        imByte.remove(position);
                        imUri.remove(position);
                        notifyDataSetChanged();
                    }
                });
            } else if (type == 2) {
                convertView  = getLayoutInflater().inflate(R.layout.tiny_ingredients_child, null);
                TextView name_ingredientTV = (TextView) convertView.findViewById(R.id.name_ingredientTV);
                name_ingredientTV.setText(jsonObjects.get(position).get("NOMBRE").getAsString());
                EditText peso_ingredientET = (EditText) convertView.findViewById(R.id.peso_ingredientET);
                ArrayAdapter<String> adapterSN = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.spinner_measure));
                adapterSN.setDropDownViewResource(R.layout.spinner_selected);
                medida_ingredientSN = (Spinner) convertView.findViewById(R.id.medida_ingredientSN);
                medida_ingredientSN.setAdapter(adapterSN);
                ImageButton delete_ingredientBTN = (ImageButton) convertView.findViewById(R.id.delete_ingredientBTN);
                delete_ingredientBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listIngredients.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
            return convertView;
        }
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

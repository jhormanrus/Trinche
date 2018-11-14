package com.trinche.app.adapters;

import android.net.Uri;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public interface MNGrecipe {

    void saveReceta(JsonObject jsonObject);
    void savePasos(JsonArray jsonArray, List<byte[]> imByte, List<Uri> imUri);
    void saveRecetaImage(byte[] file, Uri url);
}

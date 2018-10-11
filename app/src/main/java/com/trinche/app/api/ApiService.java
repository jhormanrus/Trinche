package com.trinche.app.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("user/login/")
    Call<JsonObject> loginUsuario(
            @Body JsonObject json
    );

    @POST("user/create/")
    Call<JsonObject> createUsuario(
            @Body JsonObject json
    );
}

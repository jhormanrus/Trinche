package com.trinche.app.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("user/login/")
    Call<JsonObject> loginUsuario(
            @Body JsonObject json
    );

    @POST("user/create/")
    Call<JsonObject> createUsuario(
            @Body JsonObject json
    );

    @GET("user/read/")
    Call<JsonObject> readUsuario(
            @Query("token") String TOKEN
    );

    @POST("user/update/")
    Call<JsonObject> updateUsuario(
            @Query("token") String TOKEN,
            @Body JsonObject json
    );

    @POST("user/validate/")
    Call<JsonObject> validateUsuario(
            @Query("token") String TOKEN
    );

    @Multipart
    @POST("user/perfilup/")
    Call<JsonObject> uploadUsuario(
            @Part MultipartBody.Part file,
            @Part("id_usuario") String id_usuario
    );

    @Multipart
    @POST("user/portadaup/")
    Call<JsonObject> portadaupUsuario(
            @Part MultipartBody.Part file,
            @Part("id_usuario") String id_usuario
    );

    @POST("user/password/")
    Call<JsonObject> passwordUsuario(
            @Query("token") String TOKEN,
            @Body JsonObject json
    );

    @POST("user/delete/")
    Call<JsonObject> deleteUsuario(
            @Query("token") String TOKEN,
            @Body JsonObject json
    );

    @POST("recipe/create/")
    Call<JsonObject> createReceta(
            @Query("token") String TOKEN,
            @Body JsonObject json
    );

    @Multipart
    @POST("recipe/recipeup/")
    Call<JsonObject> uploadRecipe(
            @Part MultipartBody.Part file,
            @Part("id_receta") String id_receta
    );

    @Multipart
    @POST("recipe/stepup/")
    Call<JsonObject> uploadStep(
            @Part MultipartBody.Part file,
            @Part("id_pasos") String id_pasos
    );

    @GET("recipe/readall/")
    Call<JsonArray> readallRecipe(
            @Query("token") String TOKEN,
            @Query("last_recipe") String LAST_RECIPE
    );

    @GET("recipe/readother/")
    Call<JsonArray> readotherRecipe(
            @Query("token") String TOKEN,
            @Query("last_recipe") String LAST_RECIPE,
            @Query("id_table") String ID_TABLE
    );

    @GET("recipe/readdetail/")
    Call<JsonObject> readdetailRecipe(
            @Query("token") String TOKEN,
            @Query("id_receta") String LAST_RECIPE
    );

    @GET("ranking/readscore/")
    Call<JsonObject> readscoreRanking(
            @Query("token") String TOKEN
    );

    @POST("ranking/updatescore/")
    Call<JsonObject> updatescoreRanking(
            @Query("token") String TOKEN,
            @Body JsonObject json
    );

    @GET("country/readall/")
    Call<JsonArray> readallCountry();

    @GET("category/readall/")
    Call<JsonArray> readallCategory();

    @GET("ingredient/readall/")
    Call<JsonArray> readallIngredient();
}

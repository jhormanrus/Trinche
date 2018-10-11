package com.trinche.app.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiAdapter {

    private static ApiService API_SERVICE;

    public static ApiService getApiService(String port) {

        String api_url = "http://10.147.19.237:" + port + "/";
        Gson gson = new GsonBuilder().setLenient().create();

        if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(api_url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            API_SERVICE = retrofit.create(ApiService.class);
        }
        return API_SERVICE;
    }
}

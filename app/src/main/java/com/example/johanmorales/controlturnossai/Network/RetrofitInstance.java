package com.example.johanmorales.controlturnossai.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;

    private String BASE_URL; //'https://employees-sai-backend-test.azurewebsites.net/api'

    public RetrofitInstance(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    public Retrofit getRetrofitInstance() {
        /*if (retrofit == null) {
            System.out.println("retrofit null");
        }*/

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}

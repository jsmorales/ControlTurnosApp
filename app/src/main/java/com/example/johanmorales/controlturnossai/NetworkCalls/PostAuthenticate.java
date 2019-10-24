package com.example.johanmorales.controlturnossai.NetworkCalls;


import com.example.johanmorales.controlturnossai.Models.Respuesta;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PostAuthenticate {
    @FormUrlEncoded
    @POST("authentication/")
    Call<Respuesta> authenticate(@Field("username") String username, @Field("password") String password, @Field("region") String region);
}

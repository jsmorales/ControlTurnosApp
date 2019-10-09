package com.example.johanmorales.controlturnossai.NetworkCalls;

import com.example.johanmorales.controlturnossai.Models.CheckArrivingResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PostArrivingCheck {
    @FormUrlEncoded
    @POST("api/checkArriving")
    Call<CheckArrivingResponse> postArrivingCheck(@Query("token") String token, @Field("socialNumber") String socialNumber, @Field("filterUbication") String filterUbication);
}

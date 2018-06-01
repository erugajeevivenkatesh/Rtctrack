package com.example.surya.rtctrack;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;


interface friendlist {
    @FormUrlEncoded
    @POST("/hosting.php")
    void insertUser(
            @Field("name") String name,
            @Field("range") String range,
            Callback<Response> callback);
}


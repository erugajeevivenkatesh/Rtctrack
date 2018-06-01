package com.example.surya.rtctrack;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by SURYA on 7/19/2017.
 */

public interface resetapi {

    @FormUrlEncoded
    @POST("/Resetpass.php")
    void insertUser(
            @Field("email") String email,
            @Field("password") String password,
            Callback<Response> callback);
}

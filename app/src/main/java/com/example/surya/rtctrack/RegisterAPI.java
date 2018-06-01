package com.example.surya.rtctrack;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by SURYA on 5/23/2017.
 */

public interface RegisterAPI {
    @FormUrlEncoded
    @POST("/register4.php")
    void insertUser(
            @Field("name") String name,
            @Field("lastname") String username,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("password") String password,
            Callback<Response> callback);
}

package com.example.surya.rtctrack;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by SURYA on 6/6/2017.
 */

public interface Nearby {
    @FormUrlEncoded
    @POST("/logininfo.php")
    void insertUser(
            @Field("name") String name,
            @Field("Lattitude") String lattitude,
            @Field("Longitude") String longitude,

            Callback<Response> callback);
}

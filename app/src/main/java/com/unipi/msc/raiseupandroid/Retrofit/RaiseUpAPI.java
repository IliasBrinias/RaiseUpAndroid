package com.unipi.msc.raiseupandroid.Retrofit;

import com.google.gson.JsonObject;
import com.unipi.msc.raiseupandroid.Retrofit.Request.LoginRequest;
import com.unipi.msc.raiseupandroid.Retrofit.Request.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RaiseUpAPI {
    @POST("auth/register")
    Call<JsonObject> register(@Body RegisterRequest request);
    @POST("auth/login")
    Call<JsonObject> login(@Body LoginRequest request);
    @GET("user")
    Call<JsonObject> getUser(@Header("Authorization") String auth);
}

package com.unipi.msc.raiseupandroid.Retrofit;

import com.google.gson.JsonObject;
import com.unipi.msc.raiseupandroid.Retrofit.Request.EditUserRequest;
import com.unipi.msc.raiseupandroid.Retrofit.Request.LoginRequest;
import com.unipi.msc.raiseupandroid.Retrofit.Request.RegisterRequest;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface RaiseUpAPI {
    @POST("auth/register")
    Call<JsonObject> register(@Body RegisterRequest request);
    @POST("auth/login")
    Call<JsonObject> login(@Body LoginRequest request);
    @GET("user")
    Call<JsonObject> getUser(@Header("Authorization") String auth);
    @PATCH("user")
    @Multipart
    Call<JsonObject> editUser(@Header("Authorization") String auth,
                              @Part MultipartBody.Part multipartFile,
                              @Part("password") RequestBody password,
                              @Part("firstName") RequestBody firstName,
                              @Part("lastName") RequestBody lastName);
}

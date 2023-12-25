package com.unipi.msc.raiseupandroid.Retrofit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unipi.msc.raiseupandroid.Retrofit.Request.BoardRequest;
import com.unipi.msc.raiseupandroid.Retrofit.Request.EditUserRequest;
import com.unipi.msc.raiseupandroid.Retrofit.Request.LoginRequest;
import com.unipi.msc.raiseupandroid.Retrofit.Request.RegisterRequest;
import com.unipi.msc.raiseupandroid.Retrofit.Request.TagRequest;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RaiseUpAPI {
//    Auth
    @POST("auth/register")
    Call<JsonObject> register(@Body RegisterRequest request);
    @POST("auth/login")
    Call<JsonObject> login(@Body LoginRequest request);
//    User
    @GET("user")
    Call<JsonObject> getUser(@Header("Authorization") String auth);
    @PATCH("user")
    @Multipart
    Call<JsonObject> editUser(@Header("Authorization") String auth,
                              @Part MultipartBody.Part multipartFile,
                              @Part("password") RequestBody password,
                              @Part("firstName") RequestBody firstName,
                              @Part("lastName") RequestBody lastName);
    @GET("user/search")
    Call<JsonObject> searchUser(@Header("Authorization") String auth, @Query("keyword") String keyword);

//    Tag
    @GET("tag")
    Call<JsonObject> getTags(@Header("Authorization") String auth);
    @GET("tag/{tagId}")
    Call<JsonObject> getTag(@Header("Authorization") String auth, @Path("tagId") Long tagId);
    @POST("tag")
    Call<JsonObject> createTag(@Header("Authorization") String auth, @Body TagRequest tagRequest);
    @PATCH("tag/{tagId}")
    Call<JsonObject> editTag(@Header("Authorization") String auth, @Path("tagId") Long tagId, @Body TagRequest request);
    @DELETE("tag/{tagId}")
    Call<JsonObject> deleteTag(@Header("Authorization") String auth, @Path("tagId") Long tagId);
//    Board
    @GET("board")
    Call<JsonObject> getBoards(@Header("Authorization") String auth);
    @GET("board/{boardId}")
    Call<JsonObject> getBoard(@Header("Authorization") String auth, @Path("boardId") Long boardId);
    @POST("board")
    Call<JsonObject> createBoard(@Header("Authorization") String auth, @Body BoardRequest request);
//    Task
    @GET("task/{taskId}")
    Call<JsonObject> getTask(@Header("Authorization") String auth, @Path("taskId") Long taskId);

    @GET("board/{boardId}/employees")
    Call<JsonObject> getBoardUsers(@Header("Authorization") String auth, @Path("boardId") Long boardId);
}

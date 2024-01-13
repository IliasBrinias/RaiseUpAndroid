package com.unipi.msc.riseupandroid.Retrofit;

import com.google.gson.JsonObject;
import com.unipi.msc.riseupandroid.Retrofit.Request.BoardRequest;
import com.unipi.msc.riseupandroid.Retrofit.Request.FCMRequest;
import com.unipi.msc.riseupandroid.Retrofit.Request.LoginRequest;
import com.unipi.msc.riseupandroid.Retrofit.Request.RegisterRequest;
import com.unipi.msc.riseupandroid.Retrofit.Request.StepRequest;
import com.unipi.msc.riseupandroid.Retrofit.Request.TagRequest;
import com.unipi.msc.riseupandroid.Retrofit.Request.TaskRequest;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RaiseUpAPI {
//    Auth
    @POST("auth/register")
    Call<JsonObject> register(@Body RegisterRequest request);
    @POST("auth/login")
    Call<JsonObject> login(@Body LoginRequest request);

//    User
    @GET("user")
    Call<JsonObject> getUser(@Header("Authorization") String auth);
    @GET("user/search")
    Call<JsonObject> searchUser(@Header("Authorization") String auth,
                                @Query("boardId") Long board,
                                @Query("allUsers") boolean allUsers,
                                @Query("keyword") String keyword);
    @PATCH("user/fcm")
    Call<JsonObject> updateFCM(@Header("Authorization") String auth, @Body FCMRequest request);
    @PATCH("user")
    @Multipart
    Call<JsonObject> editUser(@Header("Authorization") String auth,
                              @Part MultipartBody.Part multipartFile,
                              @Part("password") RequestBody password,
                              @Part("firstName") RequestBody firstName,
                              @Part("lastName") RequestBody lastName);
//    Tag
    @GET("tag")
    Call<JsonObject> getTags(@Header("Authorization") String auth);
    @GET("tag/{tagId}")
    Call<JsonObject> getTag(@Header("Authorization") String auth, @Path("tagId") Long tagId);
    @GET("tag/search")
    Call<JsonObject> searchTags(@Header("Authorization") String auth, @Query("keyword") String keyword);
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
    @GET("board/{boardId}/steps")
    Call<JsonObject> getColumns(@Header("Authorization") String auth, @Path("boardId") Long boardId);
    @GET("board/{boardId}/employees")
    Call<JsonObject> getBoardUsers(@Header("Authorization") String auth, @Path("boardId") Long boardId);
    @GET("board/search")
    Call<JsonObject> searchBoard(@Header("Authorization") String auth, @Query("keyword") String keyword);
    @POST("board")
    Call<JsonObject> createBoard(@Header("Authorization") String auth, @Body BoardRequest request);
    @PATCH("board/{boardId}")
    Call<JsonObject> updateBoard(@Header("Authorization") String auth, @Path("boardId") Long boardId, @Body BoardRequest boardRequest);
    @PATCH("board/{boardId}/column-order")
    Call<JsonObject> updateColumnOrder(@Header("Authorization") String auth, @Path("boardId") Long boardId, @Body List<ColumnRequest> boardRequest);
    @POST("board/{boardId}/steps")
    Call<JsonObject> addColumn(@Header("Authorization") String auth, @Path("boardId") Long boardId, @Body StepRequest name);
    @DELETE("board/{boardId}")
    Call<JsonObject> deleteBoard(@Header("Authorization") String auth, @Path("boardId") Long boardId);
//    Task
    @GET("task/{taskId}")
    Call<JsonObject> getTask(@Header("Authorization") String auth, @Path("taskId") Long taskId);
    @GET("task/{taskId}/propose-users")
    Call<JsonObject> proposeUsers(@Header("Authorization") String auth, @Path("taskId") Long taskId);
    @GET("task")
    Call<JsonObject> getTasks(@Header("Authorization") String auth);
    @GET("task/search")
    Call<JsonObject> searchTasks(@Header("Authorization") String auth, @Query("keyword") String keyword);
    @POST("task")
    Call<JsonObject> createTask(@Header("Authorization") String auth, @Body TaskRequest request);
    @PATCH("task/{taskId}")
    Call<JsonObject> updateTask(@Header("Authorization") String auth, @Path("taskId") Long taskId, @Body TaskRequest request);
    @DELETE("task/{taskId}")
    Call<JsonObject> deleteTask(@Header("Authorization") String auth, @Path("taskId") Long taskId);
//  Step
    @PATCH("step/{stepId}")
    Call<JsonObject> editColumn(@Header("Authorization") String auth, @Path("stepId") Long stepId, @Body StepRequest request);
    @DELETE("step/{stepId}")
    Call<JsonObject> deleteColumn(@Header("Authorization") String auth, @Path("stepId") Long stepId);
//  Statistics
    @GET("statistics")
    Call<JsonObject> getProgress(@Header("Authorization") String auth, @Query("dateFrom") Long dateFrom,@Query("dateTo") Long dateTo);
    @GET("statistics/user")
    Call<JsonObject> getUserStatistics(@Header("Authorization") String auth, @Query("dateFrom") Long dateFrom,@Query("dateTo") Long dateTo);
}

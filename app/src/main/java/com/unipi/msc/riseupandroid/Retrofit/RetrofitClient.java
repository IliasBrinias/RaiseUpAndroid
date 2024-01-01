package com.unipi.msc.riseupandroid.Retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.unipi.msc.riseupandroid.Retrofit.RetrofitConfig.NetworkConnectionInterceptor;
import com.unipi.msc.riseupandroid.Tools.RetrofitUtils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;
    public synchronized static Retrofit getInstance(Context c){
        if (instance == null) {
            instance = buildRetrofit(c);
        }
        return instance;
    }
    private static Retrofit buildRetrofit(Context c){

        OkHttpClient.Builder oktHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new NetworkConnectionInterceptor(c));
        Gson gson = new Gson();
        gson.serializeNulls();
        return new Retrofit.Builder()
                .baseUrl(RetrofitUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(oktHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}

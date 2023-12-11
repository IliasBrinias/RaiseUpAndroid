package com.unipi.msc.raiseupandroid.Retrofit;

import android.content.Context;

import com.unipi.msc.raiseupandroid.Retrofit.RetrofitConfig.NetworkConnectionInterceptor;
import com.unipi.msc.raiseupandroid.Tools.RetrofitUtils;

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

        return new Retrofit.Builder()
                .baseUrl(RetrofitUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(oktHttpClient.build())
                .build();
    }
}

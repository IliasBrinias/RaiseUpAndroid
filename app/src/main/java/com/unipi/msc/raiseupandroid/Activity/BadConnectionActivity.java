package com.unipi.msc.raiseupandroid.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.gson.JsonObject;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.raiseupandroid.Retrofit.RetrofitClient;
import com.unipi.msc.raiseupandroid.Tools.ActivityUtils;
import com.unipi.msc.raiseupandroid.Tools.UserUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BadConnectionActivity extends AppCompatActivity {
    private Button buttonTryAgain;
    private ProgressBar progressBar;
    private RaiseUpAPI raiseUpAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bad_connection);
        initViews();
        initObjects();
        initListeners();
    }

    private void initObjects() {
        raiseUpAPI = RetrofitClient.getInstance(this).create(RaiseUpAPI.class);
    }

    private void initListeners() {
        buttonTryAgain.setOnClickListener(this::tryAgain);
    }

    private void initViews() {
        buttonTryAgain = findViewById(R.id.buttonTryAgain);
        progressBar = findViewById(R.id.progressBar);
    }

    private void tryAgain(View view) {
        ActivityUtils.showProgressBar(progressBar);
        raiseUpAPI.getUser(UserUtils.loadBearerToken(this)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                ActivityUtils.hideProgressBar(progressBar);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ActivityUtils.hideProgressBar(progressBar);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
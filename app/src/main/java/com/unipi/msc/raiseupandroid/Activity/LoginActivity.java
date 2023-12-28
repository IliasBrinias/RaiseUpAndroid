package com.unipi.msc.raiseupandroid.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.unipi.msc.raiseupandroid.Model.User;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.raiseupandroid.Retrofit.Request.LoginRequest;
import com.unipi.msc.raiseupandroid.Retrofit.Request.RegisterRequest;
import com.unipi.msc.raiseupandroid.Retrofit.RetrofitClient;
import com.unipi.msc.raiseupandroid.Tools.ActivityUtils;
import com.unipi.msc.raiseupandroid.Tools.RetrofitUtils;
import com.unipi.msc.raiseupandroid.Tools.UserUtils;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    ImageButton imageButtonClose;
    View includeErrorMessage;
    EditText editTextUsername, editTextPassword;
    Button buttonLogin;
    TextView textViewChangePass, textViewErrorMessage;
    RaiseUpAPI raiseUpAPI;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initObjects();
        initListeners();
    }

    private void initObjects() {
        raiseUpAPI = RetrofitClient.getInstance(this).create(RaiseUpAPI.class);
    }

    private void initListeners() {
        imageButtonClose.setOnClickListener(view -> finish());
        buttonLogin.setOnClickListener(this::login);
        textViewChangePass.setOnClickListener(this::forgotPassword);
        ActivityUtils.hideProgressBar(progressBar);
        KeyboardVisibilityEvent.setEventListener(this, isOpen -> {
            if (isOpen){
                buttonLogin.setVisibility(View.GONE);
                textViewChangePass.setVisibility(View.GONE);
            }else {
                buttonLogin.setVisibility(View.VISIBLE);
                textViewChangePass.setVisibility(View.VISIBLE);
            }
        });
    }

    private void forgotPassword(View view) {
//        TODO: Forgot Password
    }

    private void login(View view) {
        ActivityUtils.showProgressBar(progressBar);
        raiseUpAPI.login(new LoginRequest(
                editTextUsername.getText().toString(),
                editTextPassword.getText().toString()
        )).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(LoginActivity.this,response);
                    if (!msg.isEmpty()){
                        textViewErrorMessage.setText(msg);
                        includeErrorMessage.setVisibility(View.VISIBLE);
                    }else{
                        includeErrorMessage.setVisibility(View.GONE);
                    }
                }else{
                    JsonObject data = response.body().get("data").getAsJsonObject();
                    User u = User.buildFromJSON(data);
                    UserUtils.saveUser(LoginActivity.this,u);
                    UserUtils.saveBearerToken(LoginActivity.this, User.getTokenFromJSON(data));
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
                ActivityUtils.hideProgressBar(progressBar);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(LoginActivity.this,t);
                ActivityUtils.hideProgressBar(progressBar);
            }
        });
    }

    private void initViews() {
        imageButtonClose = findViewById(R.id.imageButtonClose);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewChangePass = findViewById(R.id.textViewChangePass);
        includeErrorMessage = findViewById(R.id.includeErrorMessage);
        textViewErrorMessage = includeErrorMessage.findViewById(R.id.textViewErrorMessage);
        progressBar = findViewById(R.id.progressBar);
    }
}
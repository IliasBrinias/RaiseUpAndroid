package com.unipi.msc.raiseupandroid.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.unipi.msc.raiseupandroid.Retrofit.Request.RegisterRequest;
import com.unipi.msc.raiseupandroid.Retrofit.RetrofitClient;
import com.unipi.msc.raiseupandroid.Tools.ActivityUtils;
import com.unipi.msc.raiseupandroid.Tools.RetrofitUtils;
import com.unipi.msc.raiseupandroid.Tools.UserUtils;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements TextWatcher {
    ImageButton imageButtonClose;
    View includeErrorMessage;

    EditText editTextFirstName, editTextLastName,
             editTextEmail, editTextUsername,
             editTextPassword;
    Button buttonSignUp;
    TextView textViewPrivacy, textViewErrorMessage;
    RaiseUpAPI raiseUpAPI;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initObjects();
        initListeners();
    }

    private void initObjects() {
        raiseUpAPI = RetrofitClient.getInstance(this).create(RaiseUpAPI.class);
    }

    private void initListeners() {
        imageButtonClose.setOnClickListener(view -> finish());
        buttonSignUp.setOnClickListener(this::signUp);
        textViewPrivacy.setOnClickListener(v->startActivity(new Intent(this, PaymentTermsActivity.class)));
        editTextUsername.addTextChangedListener(this);
        editTextEmail.addTextChangedListener(this);
        editTextPassword.addTextChangedListener(this);
        editTextFirstName.addTextChangedListener(this);
        editTextLastName.addTextChangedListener(this);
        ActivityUtils.hideProgressBar(progressBar);
        KeyboardVisibilityEvent.setEventListener(this,isOpen -> {
            if (isOpen){
                buttonSignUp.setVisibility(View.GONE);
                textViewPrivacy.setVisibility(View.GONE);
            }else {
                buttonSignUp.setVisibility(View.VISIBLE);
                textViewPrivacy.setVisibility(View.VISIBLE);
            }
        });

    }
    private void signUp(View view) {
        ActivityUtils.showProgressBar(progressBar);
        raiseUpAPI.register(new RegisterRequest(
                editTextUsername.getText().toString(),
                editTextEmail.getText().toString(),
                editTextPassword.getText().toString(),
                editTextFirstName.getText().toString(),
                editTextLastName.getText().toString()
        )).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(RegisterActivity.this,response);
                    if (!msg.isEmpty()){
                        textViewErrorMessage.setText(msg);
                        includeErrorMessage.setVisibility(View.VISIBLE);
                    }
                }else{
                    JsonObject data = response.body().get("data").getAsJsonObject();
                    User u = User.buildFromJSON(data);
                    UserUtils.saveUser(RegisterActivity.this,u);
                    UserUtils.saveBearerToken(RegisterActivity.this, User.getTokenFromJSON(data));
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                }
                ActivityUtils.hideProgressBar(progressBar);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(RegisterActivity.this,t);
                ActivityUtils.hideProgressBar(progressBar);
            }
        });
    }

    private void initViews() {
        imageButtonClose = findViewById(R.id.imageButtonClose);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewPrivacy = findViewById(R.id.textViewPrivacy);
        includeErrorMessage = findViewById(R.id.includeErrorMessage);
        textViewErrorMessage = includeErrorMessage.findViewById(R.id.textViewErrorMessage);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    @Override
    public void afterTextChanged(Editable s) {
        boolean enable = checkIfIsAcceptable(s);
        if (buttonSignUp.isActivated() != enable) buttonSignUp.setActivated(enable);
    }

    private boolean checkIfIsAcceptable(Editable s) {
        List<Boolean> isAcceptable = new ArrayList<>();
        isAcceptable.add(!editTextFirstName.getText().toString().isEmpty());
        isAcceptable.add(!editTextLastName.getText().toString().isEmpty());
        isAcceptable.add(!editTextEmail.getText().toString().isEmpty());
        isAcceptable.add(!editTextUsername.getText().toString().isEmpty());
        isAcceptable.add(!editTextPassword.getText().toString().isEmpty());
        return !isAcceptable.contains(false);
    }
}
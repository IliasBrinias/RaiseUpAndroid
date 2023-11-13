package com.unipi.msc.raiseupandroid.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.unipi.msc.raiseupandroid.R;

public class LoginActivity extends AppCompatActivity {
    ImageButton imageButtonClose;
    EditText editTextUsername, editTextPassword;
    Button buttonLogin;
    TextView textViewChangePass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initListeners();
    }

    private void initListeners() {
        imageButtonClose.setOnClickListener(view -> finish());
        buttonLogin.setOnClickListener(this::login);
        textViewChangePass.setOnClickListener(this::forgotPassword);
    }

    private void forgotPassword(View view) {
//        TODO: Forgot Password
    }

    private void login(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void initViews() {
        imageButtonClose = findViewById(R.id.imageButtonClose);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewChangePass = findViewById(R.id.textViewChangePass);
    }
}
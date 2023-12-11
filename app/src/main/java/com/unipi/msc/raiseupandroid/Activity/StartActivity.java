package com.unipi.msc.raiseupandroid.Activity;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Tools.UserUtils;

public class StartActivity extends AppCompatActivity {
    Button buttonSignUp;
    TextView textViewLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initViews();
        if (UserUtils.userExists(this)) startActivity(new Intent(this, MainActivity.class));
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity();
            }
        });
    }
    private void initViews() {
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewLogin = findViewById(R.id.textViewLogin);
        buttonSignUp.setOnClickListener(view -> startActivity(new Intent(this, RegisterActivity.class)));
        textViewLogin.setOnClickListener(view -> startActivity(new Intent(this, LoginActivity.class)));
    }
}
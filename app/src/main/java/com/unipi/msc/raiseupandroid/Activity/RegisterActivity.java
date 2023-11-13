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

public class RegisterActivity extends AppCompatActivity {
    ImageButton imageButtonClose;
    EditText editTextFirstName, editTextLastName,
             editTextEmail, editTextUsername,
             editTextPassword;
    Button buttonSignUp;
    TextView textViewPrivacy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initListeners();
    }

    private void initListeners() {
        imageButtonClose.setOnClickListener(view -> finish());
        buttonSignUp.setOnClickListener(this::signUp);
        textViewPrivacy.setOnClickListener(this::privacyTerms);
    }

    private void privacyTerms(View view) {
//        TODO: Show Privacy Terms
    }

    private void signUp(View view) {
        startActivity(new Intent(this,MainActivity.class));
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
    }
}
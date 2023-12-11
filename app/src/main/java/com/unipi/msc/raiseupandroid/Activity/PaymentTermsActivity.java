package com.unipi.msc.raiseupandroid.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

import com.unipi.msc.raiseupandroid.R;

public class PaymentTermsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_terms);
        ((ImageButton) findViewById(R.id.imageButtonClose)).setOnClickListener(v->finish());
    }
}
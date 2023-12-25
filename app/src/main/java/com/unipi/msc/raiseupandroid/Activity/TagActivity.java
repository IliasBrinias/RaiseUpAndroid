package com.unipi.msc.raiseupandroid.Activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonObject;
import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.OnColorSelectionListener;
import com.unipi.msc.raiseupandroid.Model.Tag;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.raiseupandroid.Retrofit.Request.TagRequest;
import com.unipi.msc.raiseupandroid.Retrofit.RetrofitClient;
import com.unipi.msc.raiseupandroid.Tools.ActivityUtils;
import com.unipi.msc.raiseupandroid.Tools.NameTag;
import com.unipi.msc.raiseupandroid.Tools.RetrofitUtils;
import com.unipi.msc.raiseupandroid.Tools.UserUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagActivity extends AppCompatActivity {

    TextView textViewTitle;
    ImageButton imageButtonExit;
    ImageView imageViewSelectedColor;
    HSLColorPicker colorPicker;
    EditText editTextTagName;
    Button buttonSubmit;
    Tag tag = new Tag();
    RaiseUpAPI raiseUpAPI;
    Toast t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        initViews();
        initObjects();
        initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTag();
    }

    private void initObjects() {
        raiseUpAPI = RetrofitClient.getInstance(this).create(RaiseUpAPI.class);
    }

    private void loadTag() {
        tag.setId(getIntent().getLongExtra(NameTag.TAG_ID,0L));
        if (tag.getId() == 0L){
            textViewTitle.setText(getText(R.string.create_tag));
            int color = getColor(R.color.light_primary);
            colorPicker.setColor(color);
            imageViewSelectedColor.setColorFilter(color);
            tag.setColor("#" + Integer.toHexString(ContextCompat.getColor(this, R.color.light_primary) & 0x00ffffff));
        }else{
            textViewTitle.setText(getText(R.string.edit_tag));
            raiseUpAPI.getTag(UserUtils.loadBearerToken(this),tag.getId()).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (!response.isSuccessful()) {
                        String msg = RetrofitUtils.handleErrorResponse(TagActivity.this, response);
                        ActivityUtils.showToast(TagActivity.this, t, msg);
                    }else{
                        tag = Tag.buildFromJSON(response.body().get("data").getAsJsonObject());
                        fillData();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    RetrofitUtils.handleException(TagActivity.this,t);
                }
            });
        }
    }

    private void fillData() {
        colorPicker.setColor(Color.parseColor(tag.getColor()));
        imageViewSelectedColor.setColorFilter(Color.parseColor(tag.getColor()));
        editTextTagName.setText(tag.getName());
    }

    private void initListeners() {
        imageButtonExit.setOnClickListener(v->finish());
        colorPicker.setColorSelectionListener(new OnColorSelectionListener() {
            @Override
            public void onColorSelected(int color) {
                tag.setColor(String.format("#%06X", (0xFFFFFF & color)));
                imageViewSelectedColor.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
            @Override
            public void onColorSelectionStart(int i) {}
            @Override
            public void onColorSelectionEnd(int i) {}
        });
        editTextTagName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                buttonSubmit.setEnabled(!editTextTagName.getText().toString().isEmpty());
            }
        });
        buttonSubmit.setOnClickListener(this::saveTag);
    }

    private void saveTag(View view) {
        Callback<JsonObject> callback = new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    String msg = RetrofitUtils.handleErrorResponse(TagActivity.this, response);
                    ActivityUtils.showToast(TagActivity.this, t, msg);
                }else{
                    finish();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(TagActivity.this,t);
            }
        };
        if (tag.getId() != 0L){
            raiseUpAPI.editTag(UserUtils.loadBearerToken(this),
                    tag.getId(),
                    new TagRequest(editTextTagName.getText().toString(),tag.getColor()))
                .enqueue(callback);
        }else {
            raiseUpAPI.createTag(UserUtils.loadBearerToken(this),
                    new TagRequest(editTextTagName.getText().toString(),tag.getColor()))
                .enqueue(callback);
        }
    }

    private void initViews() {
        textViewTitle = findViewById(R.id.textViewTitle);
        imageButtonExit = findViewById(R.id.imageButtonExit);
        imageViewSelectedColor = findViewById(R.id.imageViewSelectedColor);
        colorPicker = findViewById(R.id.colorPickerHSL);
        editTextTagName = findViewById(R.id.editTextTagName);
        buttonSubmit = findViewById(R.id.buttonSubmit);
    }
}
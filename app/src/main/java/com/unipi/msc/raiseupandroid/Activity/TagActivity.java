package com.unipi.msc.raiseupandroid.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.OnColorSelectionListener;
import com.unipi.msc.raiseupandroid.Model.Tag;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Tools.NameTag;

public class TagActivity extends AppCompatActivity {

    TextView textViewTitle;
    ImageButton imageButtonExit, imageButtonRefresh;
    ImageView imageViewSelectedColor;
    HSLColorPicker colorPicker;
    EditText editTextTagName;
    Button buttonSubmit;
    Tag tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        initViews();
        initListeners();
        tag = getTag(getIntent().getLongExtra(NameTag.TAG_ID,0L));
        if (tag.getId() != 0L){
            textViewTitle.setText(getText(R.string.edit_tag));
        }else{
            textViewTitle.setText(getText(R.string.create_tag));
            imageButtonRefresh.setVisibility(View.GONE);
        }
    }

    private Tag getTag(long tagId) {
        Tag tag = new Tag(tagId,"Test","#7E7CCF");
        colorPicker.setColor(Color.parseColor(tag.getColor()));
        editTextTagName.setText(tag.getName());
        return tag;
    }

    private void initListeners() {
        imageButtonRefresh.setOnClickListener(v->editTextTagName.setText(tag.getName()));
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
                buttonSubmit.setEnabled(editable.toString().isEmpty());
            }
        });
        buttonSubmit.setOnClickListener(v-> {
            tag.setName(editTextTagName.getText().toString());
            finish();
        });
    }

    private void initViews() {
        textViewTitle = findViewById(R.id.textViewTitle);
        imageButtonExit = findViewById(R.id.imageButtonExit);
        imageViewSelectedColor = findViewById(R.id.imageViewSelectedColor);
        colorPicker = findViewById(R.id.colorPickerHSL);
        editTextTagName = findViewById(R.id.editTextTagName);
        imageButtonRefresh = findViewById(R.id.imageButtonRefresh);
        buttonSubmit = findViewById(R.id.buttonSubmit);
    }
}
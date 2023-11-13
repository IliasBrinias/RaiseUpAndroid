package com.unipi.msc.raiseupandroid.Tools;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.unipi.msc.raiseupandroid.Interface.OnEditPersonalData;
import com.unipi.msc.raiseupandroid.R;

public class CustomAlertDialog {
    public static void showEdit(Activity activity, int name, String initValue, OnEditPersonalData onEditPersonalData){

        View view = activity.getLayoutInflater().inflate(R.layout.personal_data_edit_layout, null);
//        AlertDialog alertDialog = new AlertDialog.Builder(activity).setView(view).setCancelable(true).create();
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(view);

        TextView textViewTitle = view.findViewById(R.id.textViewTitle);
        EditText editText = view.findViewById(R.id.editText);
        Button buttonSubmit = view.findViewById(R.id.buttonSubmit);
        ImageButton imageButtonRefresh = view.findViewById(R.id.imageButtonRefresh);
        ImageButton imageButtonClose = view.findViewById(R.id.imageButtonClose);

        textViewTitle.setText(activity.getString(R.string.change)+" "+activity.getString(name));
        editText.setText(initValue);
        buttonSubmit.setOnClickListener(v->{
            onEditPersonalData.onChange(editText.getText().toString());
            dialog.cancel();
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()){
                    if (buttonSubmit.isActivated()){
                        buttonSubmit.setActivated(false);
                    }
                }else{
                    if (!buttonSubmit.isActivated()){
                        buttonSubmit.setActivated(true);
                    }
                }
            }
        });
        imageButtonRefresh.setOnClickListener(v->editText.setText(initValue));
        imageButtonClose.setOnClickListener(v->dialog.cancel());

        dialog.show();
    }
}

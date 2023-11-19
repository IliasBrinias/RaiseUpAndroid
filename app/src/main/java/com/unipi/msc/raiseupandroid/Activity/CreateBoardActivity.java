package com.unipi.msc.raiseupandroid.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Tools.CustomBottomSheet;

public class CreateBoardActivity extends AppCompatActivity {
    ImageButton imageButtonClose, imageButtonAddEmployees, imageButtonAddColumn;
    EditText editTextBoardName;
    RecyclerView recyclerViewEmployees, recyclerViewColumns;
    Button buttonCreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);
        initView();
        initListeners();
    }

    private void initListeners() {
        imageButtonClose.setOnClickListener(view->finish());
        imageButtonAddEmployees.setOnClickListener(this::addEmployees);
        imageButtonAddColumn.setOnClickListener(this::addColumn);
    }

    private void addColumn(View view) {
        CustomBottomSheet.AddBoardColumn(this,column -> {});
    }

    private void addEmployees(View view) {
        CustomBottomSheet.addEmployees(this,employeesId -> {

        });
    }

    private void initView() {
        imageButtonClose = findViewById(R.id.imageButtonClose);
        imageButtonAddEmployees = findViewById(R.id.imageButtonAddEmployees);
        imageButtonAddColumn = findViewById(R.id.imageButtonAddColumn);
        editTextBoardName = findViewById(R.id.editTextBoardName);
        recyclerViewEmployees = findViewById(R.id.recyclerViewEmployees);
        recyclerViewColumns = findViewById(R.id.recyclerViewColumns);
        buttonCreate = findViewById(R.id.buttonCreate);
    }
}
package com.unipi.msc.raiseupandroid.Tools;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.unipi.msc.raiseupandroid.Adapter.AddEmployeeAdapter;
import com.unipi.msc.raiseupandroid.Interface.OnAddColumnResponse;
import com.unipi.msc.raiseupandroid.Interface.OnAddEmployeesResponse;
import com.unipi.msc.raiseupandroid.Interface.OnEditPersonalData;
import com.unipi.msc.raiseupandroid.Interface.OnSingleValueResponse;
import com.unipi.msc.raiseupandroid.Model.Employee;
import com.unipi.msc.raiseupandroid.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

public class CustomBottomSheet {
    public static void showEdit(Activity activity, String name, String initValue, OnEditPersonalData onEditPersonalData){
        showSingleValue(activity,activity.getString(R.string.change)+" "+name,initValue,onEditPersonalData::onChange);
    }
    private static void showSingleValue(Activity activity, String name, String initValue, OnSingleValueResponse onSingleValueResponse){

        View view = activity.getLayoutInflater().inflate(R.layout.personal_data_edit_layout, null);
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(view);

        TextView textViewTitle = view.findViewById(R.id.textViewTitle);
        EditText editText = view.findViewById(R.id.editText);
        Button buttonSubmit = view.findViewById(R.id.buttonSubmit);
        ImageButton imageButtonRefresh = view.findViewById(R.id.imageButtonRefresh);
        ImageButton imageButtonClose = view.findViewById(R.id.imageButtonClose);

        textViewTitle.setText(name);
        editText.setText(initValue);
        buttonSubmit.setOnClickListener(v->{
            onSingleValueResponse.onResponse(editText.getText().toString());
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
    public static void addEmployees(Activity activity, OnAddEmployeesResponse onAddEmployeesResponse){
        List<Employee> employees = new ArrayList<>();
        Map<Long,Boolean> selectedEmployees = new HashMap<>();
        View view = activity.getLayoutInflater().inflate(R.layout.add_employee_layout, null);
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(view);

        EditText editTextSearch = view.findViewById(R.id.editTextSearch);
        Button buttonSubmit = view.findViewById(R.id.buttonSubmit);
        ImageButton imageButtonRefresh = view.findViewById(R.id.imageButtonRefresh);
        ImageButton imageButtonClose = view.findViewById(R.id.imageButtonClose);
        RecyclerView recyclerViewAddEmployees = view.findViewById(R.id.recyclerViewAddEmployees);

        AddEmployeeAdapter addEmployeeAdapter = new AddEmployeeAdapter(activity,employees,(v, position) -> {
            selectedEmployees.put(employees.get(position).getId(), view.isSelected());
            buttonSubmit.setActivated(selectedEmployees.containsValue(true));
        });

        recyclerViewAddEmployees.setAdapter(addEmployeeAdapter);
        recyclerViewAddEmployees.setLayoutManager(new LinearLayoutManager(activity));

        imageButtonRefresh.setOnClickListener(v->addEmployeeAdapter.resetItems());

        imageButtonClose.setOnClickListener(v->dialog.cancel());

        buttonSubmit.setOnClickListener(v->{
            List<Long> ids = new ArrayList<>();
            selectedEmployees.entrySet().stream().filter(Map.Entry::getValue).forEach(entry->ids.add(entry.getKey()));
            onAddEmployeesResponse.onResponse(ids);
            dialog.cancel();
        });

        dialog.show();
    }

    public static void AddBoardColumn(Activity activity, OnAddColumnResponse onAddColumnResponse) {
        View view = activity.getLayoutInflater().inflate(R.layout.add_progress_column_layout, null);
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(view);

        EditText editTextColumn = view.findViewById(R.id.editTextProgressColumn);
        ImageButton imageButtonAddColumn = view.findViewById(R.id.imageButtonAddColumn);
        imageButtonAddColumn.setOnClickListener(v->{
            if (!editTextColumn.getText().toString().isEmpty()){
                onAddColumnResponse.onResponse(editTextColumn.getText().toString());
            }
            dialog.cancel();
        });
        dialog.show();
    }
}

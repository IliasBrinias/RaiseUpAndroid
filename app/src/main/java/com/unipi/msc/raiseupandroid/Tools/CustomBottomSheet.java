package com.unipi.msc.raiseupandroid.Tools;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unipi.msc.raiseupandroid.Activity.RegisterActivity;
import com.unipi.msc.raiseupandroid.Adapter.AddEmployeeAdapter;
import com.unipi.msc.raiseupandroid.Interface.OnAddColumnResponse;
import com.unipi.msc.raiseupandroid.Interface.OnAddEmployeesResponse;
import com.unipi.msc.raiseupandroid.Interface.OnEditPersonalData;
import com.unipi.msc.raiseupandroid.Interface.OnSingleValueResponse;
import com.unipi.msc.raiseupandroid.Model.User;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.raiseupandroid.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public static void addEmployees(Activity activity, List<User> alreadySelected, OnAddEmployeesResponse onAddEmployeesResponse){
        List<User> users = new ArrayList<>();
        Map<Long,Boolean> selectedEmployees = new HashMap<>();
        RaiseUpAPI raiseUpAPI = RetrofitClient.getInstance(activity).create(RaiseUpAPI.class);
        Toast t = null;

        View view = activity.getLayoutInflater().inflate(R.layout.add_employee_layout, null);
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(view);

        EditText editTextSearch = view.findViewById(R.id.editTextSearch);
        Button buttonSubmit = view.findViewById(R.id.buttonSubmit);
        ImageButton imageButtonRefresh = view.findViewById(R.id.imageButtonRefresh);
        ImageButton imageButtonClose = view.findViewById(R.id.imageButtonClose);
        RecyclerView recyclerViewAddEmployees = view.findViewById(R.id.recyclerViewAddEmployees);

        AddEmployeeAdapter addEmployeeAdapter = new AddEmployeeAdapter(activity, users,(v, position) -> {
            selectedEmployees.put(users.get(position).getId(), v.isSelected());
            buttonSubmit.setActivated(selectedEmployees.containsValue(true));
        });

        Runnable runnable = () -> raiseUpAPI.searchUser(UserUtils.loadBearerToken(activity), editTextSearch.getText().toString())
            .enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (!response.isSuccessful()){
                        String msg = RetrofitUtils.handleErrorResponse(activity,response);
                        ActivityUtils.showToast(activity,t,msg);
                    }else {
                        users.clear();
                        JsonArray jsonArray = response.body().get("data").getAsJsonArray();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                            users.add(User.buildFromJSON(jsonObject));
                        }
                        addEmployeeAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    RetrofitUtils.handleException(activity, t);
                }
        });
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                runnable.run();
            }
        });
        recyclerViewAddEmployees.setLayoutManager(new LinearLayoutManager(activity));

        recyclerViewAddEmployees.setAdapter(addEmployeeAdapter);

        imageButtonRefresh.setOnClickListener(v->addEmployeeAdapter.resetItems());

        imageButtonClose.setOnClickListener(v->dialog.cancel());

        buttonSubmit.setOnClickListener(v->{
            List<User> selectedUsers = users.stream().filter(user -> {
                if (selectedEmployees.containsKey(user.getId())){
                    return selectedEmployees.get(user.getId());
                }
                return false;
            }).collect(Collectors.toList());
            onAddEmployeesResponse.onResponse(selectedUsers);
            dialog.cancel();
        });

        runnable.run();

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

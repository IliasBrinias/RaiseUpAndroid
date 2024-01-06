package com.unipi.msc.riseupandroid.Tools;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unipi.msc.riseupandroid.Activity.SaveBoardActivity;
import com.unipi.msc.riseupandroid.Adapter.AddEmployeeAdapter;
import com.unipi.msc.riseupandroid.Adapter.BoardCreationColumnAdapter;
import com.unipi.msc.riseupandroid.Adapter.ChangeColumnAdapter;
import com.unipi.msc.riseupandroid.Adapter.TagAdapter;
import com.unipi.msc.riseupandroid.Interface.OnAddColumnResponse;
import com.unipi.msc.riseupandroid.Interface.OnAddEmployeesResponse;
import com.unipi.msc.riseupandroid.Interface.OnBoardColumnClick;
import com.unipi.msc.riseupandroid.Interface.OnBoardPropertiesResponse;
import com.unipi.msc.riseupandroid.Interface.OnColumnChange;
import com.unipi.msc.riseupandroid.Interface.OnColumnOrderChange;
import com.unipi.msc.riseupandroid.Interface.OnDeleteClick;
import com.unipi.msc.riseupandroid.Interface.OnEditPersonalData;
import com.unipi.msc.riseupandroid.Interface.OnSingleValueResponse;
import com.unipi.msc.riseupandroid.Interface.OnTagSelected;
import com.unipi.msc.riseupandroid.Interface.OnTaskNameResponse;
import com.unipi.msc.riseupandroid.Model.Column;
import com.unipi.msc.riseupandroid.Model.Tag;
import com.unipi.msc.riseupandroid.Model.Task;
import com.unipi.msc.riseupandroid.Model.User;
import com.unipi.msc.riseupandroid.R;
import com.unipi.msc.riseupandroid.Retrofit.ColumnRequest;
import com.unipi.msc.riseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.riseupandroid.Retrofit.Request.BoardRequest;
import com.unipi.msc.riseupandroid.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomBottomSheet {
    public static final int ADD_EMPLOYEES = 0;
    public static final int CHANGE_ORDER = 1;
    public static final int DELETE_BOARD = 2;

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
        showKeyboard(activity, editText);
    }
    public static void addEmployees(Activity activity, List<User> alreadySelected, Long boardId, OnAddEmployeesResponse onAddEmployeesResponse){
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

        AddEmployeeAdapter addEmployeeAdapter = new AddEmployeeAdapter(activity, alreadySelected, users,(v, position) -> {
            selectedEmployees.put(users.get(position).getId(), v.isSelected());
            buttonSubmit.setActivated(selectedEmployees.containsValue(true));
        });
        Runnable runnable = () -> raiseUpAPI.searchUser(UserUtils.loadBearerToken(activity), boardId, editTextSearch.getText().toString())
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
        singleInput(activity,activity.getString(R.string.progress_column), onAddColumnResponse::onResponse);
    }
    public static void AddTaskName(Activity activity, OnTaskNameResponse onAddColumnResponse) {
        singleInput(activity,activity.getString(R.string.task_name), onAddColumnResponse::onResponse);
    }
    private static void singleInput(Activity activity, String hint, OnSingleValueResponse onSingleValueResponse){
        View view = activity.getLayoutInflater().inflate(R.layout.single_input_layout, null);
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(view);

        EditText editTextInput = view.findViewById(R.id.editTextInput);
        ImageButton imageButtonConfirm = view.findViewById(R.id.imageButtonConfirm);
        editTextInput.setHint(hint);
        imageButtonConfirm.setOnClickListener(v->{
            if (!editTextInput.getText().toString().isEmpty()){
                onSingleValueResponse.onResponse(editTextInput.getText().toString());
            }
            dialog.cancel();
        });
        dialog.show();
        showKeyboard(activity, editTextInput);
    }
    public static void addTags(Activity activity, OnTagSelected onTagSelected){

        List<Tag> tags = new ArrayList<>();
        Map<Long,Boolean> selectedTags = new HashMap<>();
        RaiseUpAPI raiseUpAPI = RetrofitClient.getInstance(activity).create(RaiseUpAPI.class);


        View view = activity.getLayoutInflater().inflate(R.layout.add_tag_layout, null);
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(view);

        view.findViewById(R.id.imageButtonClose).setOnClickListener(v->dialog.cancel());
        EditText editTextSearch = view.findViewById(R.id.editTextSearch);
        Button buttonSubmit = view.findViewById(R.id.buttonSubmit);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        TagAdapter tagAdapter = new TagAdapter(activity, new ArrayList<>(), R.layout.add_tag_line_layout, (v, position) -> {
            selectedTags.put(tags.get(position).getId(), v.isSelected());
            buttonSubmit.setActivated(selectedTags.containsValue(true));
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(tagAdapter);
        Callback<JsonObject> response = new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(activity,response);
                    ActivityUtils.showToast(activity, new Toast(activity), msg);
                }else{
                    JsonArray jsonArray = response.body().get("data").getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        tags.add(Tag.buildFromJSON(jsonArray.get(i).getAsJsonObject()));
                    }
                    tagAdapter.setData(tags);
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(activity, t);
            }
        };
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                raiseUpAPI.searchTags(UserUtils.loadBearerToken(activity), s.toString()).enqueue(response);
            }
        });
        raiseUpAPI.getTags(UserUtils.loadBearerToken(activity)).enqueue(response);
        buttonSubmit.setOnClickListener(v->{
            List<Long> tagIds = new ArrayList<>();
            selectedTags.entrySet().stream().filter(Map.Entry::getValue).allMatch(entry -> tagIds.add(entry.getKey()));
            onTagSelected.onSelect(tagIds);
            dialog.cancel();
        });
        dialog.show();
    }
    public static void changeColumn(Activity activity, Task task, OnColumnChange onColumnChange){

        List<Column> columns = new ArrayList<>();
        RaiseUpAPI raiseUpAPI = RetrofitClient.getInstance(activity).create(RaiseUpAPI.class);

        View view = activity.getLayoutInflater().inflate(R.layout.change_column_layout, null);
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(view);

        view.findViewById(R.id.imageButtonClose).setOnClickListener(v->dialog.cancel());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        ChangeColumnAdapter changeColumnAdapter = new ChangeColumnAdapter(activity, task.getColumn(), columns, (v,position) -> {
            onColumnChange.onChange(columns.get(position).getId());
            dialog.cancel();
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(changeColumnAdapter);
        raiseUpAPI.getColumns(UserUtils.loadBearerToken(activity), task.getColumn().getBoardId()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(activity,response);
                    ActivityUtils.showToast(activity, new Toast(activity), msg);
                }else{
                    columns.clear();
                    JsonArray jsonArray = response.body().get("data").getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        columns.add(Column.buildFromJSON(jsonArray.get(i).getAsJsonObject()));
                    }
                    changeColumnAdapter.refreshData();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(activity, t);
            }
        });
        dialog.show();
    }
    public static void deleteMessage(Activity activity, OnDeleteClick onDeleteClick) {

        View view = activity.getLayoutInflater().inflate(R.layout.delete_layout, null);
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(view);

        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        Button buttonDelete = view.findViewById(R.id.buttonDelete);
        buttonCancel.setOnClickListener(v->dialog.cancel());
        buttonDelete.setOnClickListener(v-> {
            dialog.cancel();
            onDeleteClick.onClick();
        });
        dialog.show();
    }
    private static void showKeyboard(Activity activity, EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }
    public static void successMessage(Activity activity, String message) {
        View view = activity.getLayoutInflater().inflate(R.layout.success_layout, null);
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(view);
        TextView textViewSuccessMessage = view.findViewById(R.id.textViewSuccessMessage);
        textViewSuccessMessage.setText(message + " " + activity.getString(R.string.was_created_successfully));
        dialog.show();
    }
    public static void getBoardProperties(Activity activity, OnBoardPropertiesResponse onBoardPropertiesResponse){
        View view = activity.getLayoutInflater().inflate(R.layout.board_properties_layout, null);
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(view);
        TextView textViewAddEmployees = view.findViewById(R.id.textViewAddEmployees);
        TextView textViewChangeColumnOrder = view.findViewById(R.id.textViewChangeColumnOrder);
        TextView textViewDeleteBoard = view.findViewById(R.id.textViewDeleteBoard);
        textViewAddEmployees.setOnClickListener(v->{
            onBoardPropertiesResponse.onClick(ADD_EMPLOYEES);
            dialog.cancel();
        });
        textViewChangeColumnOrder.setOnClickListener(v->{
            onBoardPropertiesResponse.onClick(CHANGE_ORDER);
            dialog.cancel();
        });
        textViewDeleteBoard.setOnClickListener(v->{
            onBoardPropertiesResponse.onClick(DELETE_BOARD);
            dialog.cancel();
        });
        dialog.show();
    }
    public static void changeColumnOrder(Activity activity, Long boardId, OnColumnOrderChange onColumnOrderChange){

        List<Column> columns = new ArrayList<>();

        View view = activity.getLayoutInflater().inflate(R.layout.change_column_order, null);
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(view);
        ImageButton imageButtonClose = view.findViewById(R.id.imageButtonClose);
        RecyclerView recyclerViewColumns = view.findViewById(R.id.recyclerViewColumns);
        Button buttonSave = view.findViewById(R.id.buttonSave);

        RaiseUpAPI raiseUpAPI = RetrofitClient.getInstance(activity).create(RaiseUpAPI.class);
        BoardCreationColumnAdapter columnAdapter = new BoardCreationColumnAdapter(activity, columns, null);

        recyclerViewColumns.setLayoutManager(new LinearLayoutManager(activity));
        recyclerViewColumns.setAdapter(columnAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN|ItemTouchHelper.UP,0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Collections.swap(columns,viewHolder.getAdapterPosition(),target.getAdapterPosition());
                columnAdapter.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {}
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewColumns);

        raiseUpAPI.getColumns(UserUtils.loadBearerToken(activity),boardId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(activity,response);
                    ActivityUtils.showToast(activity, new Toast(activity), msg);
                }else{
                    columns.clear();
                    JsonArray jsonArray = response.body().get("data").getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        columns.add(Column.buildFromJSON(jsonArray.get(i).getAsJsonObject()));
                    }
                    columnAdapter.refreshData();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(activity, t);
            }
        });

        imageButtonClose.setOnClickListener(v->dialog.cancel());
        buttonSave.setOnClickListener(v->{
            onColumnOrderChange.onResponse(columns);
            dialog.cancel();
        });
        dialog.show();
    }
}

package com.unipi.msc.raiseupandroid.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unipi.msc.raiseupandroid.Adapter.EmployeeAdapter;
import com.unipi.msc.raiseupandroid.Adapter.TagAdapter;
import com.unipi.msc.raiseupandroid.Model.Task;
import com.unipi.msc.raiseupandroid.Model.User;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.raiseupandroid.Retrofit.Request.TaskRequest;
import com.unipi.msc.raiseupandroid.Retrofit.RetrofitClient;
import com.unipi.msc.raiseupandroid.Tools.ActivityUtils;
import com.unipi.msc.raiseupandroid.Tools.CustomBottomSheet;
import com.unipi.msc.raiseupandroid.Tools.CustomDatePicker;
import com.unipi.msc.raiseupandroid.Tools.NameTag;
import com.unipi.msc.raiseupandroid.Tools.RecyclerViewUtils;
import com.unipi.msc.raiseupandroid.Tools.RetrofitUtils;
import com.unipi.msc.raiseupandroid.Tools.UserUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskActivity extends AppCompatActivity {
    private TextView textViewDaysToExpire, textViewColumnName, textViewTaskTitle, textViewDescription, textViewToExpire;
    private RecyclerView recyclerViewTags, recyclerViewEmployees, recyclerViewRecommendedEmployees;
    private ImageButton imageButtonExit, imageButtonAddEmployees, imageButtonAddTag;
    private ProgressBar progressBar;
    private LinearLayout linearLayoutDueDate, linearLayoutCompleted;
    private View expireLayout;
    private EmployeeAdapter employeeAdapter;
    private TagAdapter tagAdapter;
    private Task task;
    private RaiseUpAPI raiseUpAPI;
    private Toast t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        initViews();
        initObjects();
        initListeners();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (tagAdapter!=null){
            getTask(getIntent().getLongExtra(NameTag.TASK_ID,0L));
        }
    }
    private void initViews() {
        imageButtonExit = findViewById(R.id.imageButtonExit);
        textViewDaysToExpire = findViewById(R.id.textViewDaysToExpire);
        textViewColumnName = findViewById(R.id.textViewColumnName);
        textViewTaskTitle = findViewById(R.id.textViewTaskTitle);
        textViewDescription = findViewById(R.id.textViewDescription);
        recyclerViewTags = findViewById(R.id.recyclerViewTags);
        recyclerViewEmployees = findViewById(R.id.recyclerViewEmployees);
        recyclerViewRecommendedEmployees = findViewById(R.id.recyclerViewRecommendedEmployees);
        imageButtonAddEmployees = findViewById(R.id.imageButtonAddEmployees);
        progressBar = findViewById(R.id.progressBar);
        expireLayout = findViewById(R.id.expireLayout);
        linearLayoutDueDate = findViewById(R.id.linearLayoutDueDate);
        imageButtonAddTag = findViewById(R.id.imageButtonAddTag);
        textViewToExpire = findViewById(R.id.textViewToExpire);
        linearLayoutCompleted = findViewById(R.id.linearLayoutCompleted);
        ActivityUtils.hideProgressBar(progressBar);
    }
    private void initObjects() {
        task = new Task();
        tagAdapter = new TagAdapter(this,task.getTags());
        employeeAdapter = new EmployeeAdapter(this,task.getUsers(),(view, position) -> removeEmployee(position));
        raiseUpAPI = RetrofitClient.getInstance(this).create(RaiseUpAPI.class);
    }
    private void initListeners() {
        imageButtonExit.setOnClickListener(view->finish());
        imageButtonAddEmployees.setOnClickListener(this::addEmployees);
        recyclerViewTags.setLayoutManager(RecyclerViewUtils.getFlexLayout(this));
        recyclerViewTags.setAdapter(tagAdapter);
        recyclerViewEmployees.setLayoutManager(RecyclerViewUtils.getFlexLayout(this));
        recyclerViewEmployees.setAdapter(employeeAdapter);
        textViewDescription.setOnClickListener(view->CustomBottomSheet.showEdit(TaskActivity.this,getString(R.string.description),textViewDescription.getText().toString(),dsc->updateTask(new TaskRequest.Builder().setDescription(dsc).build())));
        linearLayoutDueDate.setOnClickListener(view->CustomDatePicker.showPicker(TaskActivity.this,task.getDueDate(), date -> updateTask(new TaskRequest.Builder().setDueTo(date).build())));
        textViewTaskTitle.setOnClickListener(view->CustomBottomSheet.showEdit(TaskActivity.this,getString(R.string.task_name),textViewTaskTitle.getText().toString(),title->updateTask(new TaskRequest.Builder().setTitle(title).build())));
        imageButtonAddTag.setOnClickListener(view->CustomBottomSheet.addTags(TaskActivity.this,tagIds -> updateTask(new TaskRequest.Builder().setTagIds(tagIds).build())));
        linearLayoutCompleted.setOnClickListener(view -> updateTask(new TaskRequest.Builder().setCompleted(!linearLayoutCompleted.isSelected()).build()));
        textViewColumnName.setOnClickListener((view -> CustomBottomSheet.changeColumn(TaskActivity.this,task,columnId -> updateTask(new TaskRequest.Builder().setColumnId(columnId).build()) )));
    }
    private void getTask(Long taskId) {
        if (taskId == 0L) {
            finish();
            return;
        }
        ActivityUtils.showProgressBar(progressBar);
        raiseUpAPI.getTask(UserUtils.loadBearerToken(this),taskId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(TaskActivity.this,response);
                    ActivityUtils.showToast(TaskActivity.this, t, msg);
                }else{
                    loadData(Task.buildTaskFromJSON(response.body().get("data").getAsJsonObject()));
                }
                ActivityUtils.hideProgressBar(progressBar);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(TaskActivity.this, t);
                ActivityUtils.hideProgressBar(progressBar);
            }
        });
    }
    private void loadData(Task task) {
        this.task = task;
        textViewTaskTitle.setText(task.getTitle());
        expireLayout.setVisibility(View.INVISIBLE);
        if (task.getDueDate() != null){
            long days = ActivityUtils.getDifferenceDays(new Date().getTime(),task.getDueDate());
            expireLayout.setVisibility(days<=0 ? View.VISIBLE : View.INVISIBLE);
            textViewDaysToExpire.setText(Math.abs(days) + " " +(days==1?getString(R.string.day):getString(R.string.days)));
        }
        textViewToExpire.setVisibility(expireLayout.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.GONE);
        textViewColumnName.setText(task.getColumn().getTitle());
        textViewDescription.setText(task.getDescription());
        tagAdapter.setData(this.task.getTags());
        employeeAdapter.setData(this.task.getUsers());
        linearLayoutCompleted.setSelected(task.getCompleted());
    }
    private void removeEmployee(int position) {
        ActivityUtils.showProgressBar(progressBar);
        raiseUpAPI.removeUserFromTask(UserUtils.loadBearerToken(this),  task.getId(), task.getUsers().get(position).getId()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(TaskActivity.this,response);
                    ActivityUtils.showToast(TaskActivity.this,t,msg);
                }else {
                    loadData(Task.buildTaskFromJSON(response.body().get("data").getAsJsonObject()));
                }
                ActivityUtils.hideProgressBar(progressBar);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(TaskActivity.this, t);
                ActivityUtils.hideProgressBar(progressBar);
            }
        });
    }
    private void addEmployees(View view) {
        ActivityUtils.showProgressBar(progressBar);
        raiseUpAPI.getBoardUsers(UserUtils.loadBearerToken(this),task.getColumn().getBoardId()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(TaskActivity.this,response);
                    ActivityUtils.showToast(TaskActivity.this,t,msg);
                }else {
                    List<User> users = new ArrayList<>();
                    JsonArray jsonArray = response.body().get("data").getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        users.add(User.buildFromJSON(jsonArray.get(i).getAsJsonObject()));
                    }
                    CustomBottomSheet.addEmployees(TaskActivity.this, users, TaskActivity.this::updateEmployees);
                }
                ActivityUtils.hideProgressBar(progressBar);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(TaskActivity.this, t);
                ActivityUtils.hideProgressBar(progressBar);
            }
        });
    }
    private void updateEmployees(List<User> users) {
        List<Long> employeesIds = new ArrayList<>();
        users.forEach(user->employeesIds.add(user.getId()));
        updateTask(new TaskRequest.Builder().setEmployeeIds(employeesIds).build());
    }
    private void updateTask(TaskRequest request){
        ActivityUtils.showProgressBar(progressBar);
        raiseUpAPI.updateTask(UserUtils.loadBearerToken(this), task.getId(), request).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(TaskActivity.this,response);
                    ActivityUtils.showToast(TaskActivity.this, t, msg);
                }else {
                    loadData(Task.buildTaskFromJSON(response.body().get("data").getAsJsonObject()));
                }
                ActivityUtils.hideProgressBar(progressBar);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(TaskActivity.this,t);
                ActivityUtils.hideProgressBar(progressBar);
            }
        });
    }
}
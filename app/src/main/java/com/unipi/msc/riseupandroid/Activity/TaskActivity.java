package com.unipi.msc.riseupandroid.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.unipi.msc.riseupandroid.Adapter.EmployeeAdapter;
import com.unipi.msc.riseupandroid.Adapter.RecommendedEmployeeAdapter;
import com.unipi.msc.riseupandroid.Adapter.TagAdapter;
import com.unipi.msc.riseupandroid.Interface.OnTagClick;
import com.unipi.msc.riseupandroid.Model.Task;
import com.unipi.msc.riseupandroid.Model.User;
import com.unipi.msc.riseupandroid.R;
import com.unipi.msc.riseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.riseupandroid.Retrofit.Request.TaskRequest;
import com.unipi.msc.riseupandroid.Retrofit.RetrofitClient;
import com.unipi.msc.riseupandroid.Tools.ActivityUtils;
import com.unipi.msc.riseupandroid.Tools.CustomBottomSheet;
import com.unipi.msc.riseupandroid.Tools.CustomDatePicker;
import com.unipi.msc.riseupandroid.Tools.NameTag;
import com.unipi.msc.riseupandroid.Tools.RecyclerViewUtils;
import com.unipi.msc.riseupandroid.Tools.RetrofitUtils;
import com.unipi.msc.riseupandroid.Tools.UserUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskActivity extends AppCompatActivity {
    private TextView textViewDaysToExpire, textViewColumnName, textViewTaskTitle, textViewDescription, textViewDifficulty;
    private RecyclerView recyclerViewTags, recyclerViewEmployees, recyclerViewRecommendedEmployees;
    private ImageButton imageButtonExit, imageButtonAddEmployees, imageButtonAddTag;
    private ProgressBar progressBar, progressBarRecommendation;
    private LinearLayout linearLayoutDueDate, linearLayoutCompleted;
    private ConstraintLayout constraintLayoutRecommendation;
    private View expireLayout;
    private EmployeeAdapter employeeAdapter;
    private RecommendedEmployeeAdapter recommendedEmployeeAdapter;
    private TagAdapter tagAdapter;
    private Task task;
    private RaiseUpAPI raiseUpAPI;
    private Toast t;
    private List<User> recommendedUsers = new ArrayList<>();
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
        getTask(getIntent().getLongExtra(NameTag.TASK_ID,0L));
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
        progressBarRecommendation = findViewById(R.id.progressBarRecommendation);
        expireLayout = findViewById(R.id.expireLayout);
        linearLayoutDueDate = findViewById(R.id.linearLayoutDueDate);
        imageButtonAddTag = findViewById(R.id.imageButtonAddTag);
        textViewDifficulty = findViewById(R.id.textViewDifficulty);
        linearLayoutCompleted = findViewById(R.id.linearLayoutCompleted);
        constraintLayoutRecommendation = findViewById(R.id.constraintLayoutRecommendation);
    }
    private void initObjects() {
        task = new Task();
        tagAdapter = new TagAdapter(this, task.getTags(), new OnTagClick() {
            @Override
            public void onClick(View view, int position) {}
            @Override
            public void onDelete(View view, int position) {
                removeTag(position);
            }
        });
        employeeAdapter = new EmployeeAdapter(this,task.getUsers(),(view, position) -> removeEmployee(position));
        recommendedEmployeeAdapter = new RecommendedEmployeeAdapter(this,recommendedUsers,(view, position) -> {
            List<Long> userIds = new ArrayList<>();
            task.getUsers().add(recommendedUsers.get(position));
            for (User user:task.getUsers()){
                userIds.add(user.getId());
            }

            updateTask(new TaskRequest.Builder().setEmployeeIds(userIds).build());
            recommendedEmployeeAdapter.remove(position);
            if (recommendedUsers.isEmpty()){
                constraintLayoutRecommendation.setVisibility(View.GONE);
            }
        });
        raiseUpAPI = RetrofitClient.getInstance(this).create(RaiseUpAPI.class);
    }
    private void initListeners() {
        imageButtonExit.setOnClickListener(view->finish());
        imageButtonAddEmployees.setOnClickListener(this::addEmployees);
        recyclerViewTags.setLayoutManager(RecyclerViewUtils.getFlexLayout(this));
        recyclerViewTags.setAdapter(tagAdapter);
        recyclerViewEmployees.setLayoutManager(RecyclerViewUtils.getFlexLayout(this));
        recyclerViewEmployees.setAdapter(employeeAdapter);
        recyclerViewRecommendedEmployees.setLayoutManager(RecyclerViewUtils.getFlexLayout(this));
        recyclerViewRecommendedEmployees.setAdapter(recommendedEmployeeAdapter);
        textViewDescription.setOnClickListener(view->CustomBottomSheet.showEdit(TaskActivity.this,getString(R.string.description),textViewDescription.getText().toString(),dsc->updateTask(new TaskRequest.Builder().setDescription(dsc).build())));
        linearLayoutDueDate.setOnClickListener(view->CustomDatePicker.showPicker(TaskActivity.this,task.getDueDate(), date -> updateTask(new TaskRequest.Builder().setDueTo(date).build())));
        textViewTaskTitle.setOnClickListener(view->CustomBottomSheet.showEdit(TaskActivity.this,getString(R.string.task_name),textViewTaskTitle.getText().toString(),title->updateTask(new TaskRequest.Builder().setTitle(title).build())));
        imageButtonAddTag.setOnClickListener(view->CustomBottomSheet.addTags(TaskActivity.this,tagIds -> updateTask(new TaskRequest.Builder().setTagIds(tagIds).build())));
        linearLayoutCompleted.setOnClickListener(view -> updateTask(new TaskRequest.Builder().setCompleted(!linearLayoutCompleted.isSelected()).build()));
        textViewColumnName.setOnClickListener((view -> CustomBottomSheet.changeColumn(TaskActivity.this,task,columnId -> updateTask(new TaskRequest.Builder().setColumnId(columnId).build()) )));
        textViewDifficulty.setOnClickListener(v -> CustomBottomSheet.changeDifficulty(TaskActivity.this, difficulty ->updateTask(new TaskRequest.Builder().setDifficulty(difficulty).build())));
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
            expireLayout.setVisibility(days<=0 && !task.getCompleted() ? View.VISIBLE : View.INVISIBLE);
            textViewDaysToExpire.setText(ActivityUtils.normalizeDate(task.getDueDate()));
        }
        textViewColumnName.setText(task.getColumn().getTitle());
        textViewDescription.setText(task.getDescription());
        textViewDifficulty.setText(ActivityUtils.getDifficultyDsc(TaskActivity.this, task.getDifficulty()));
        tagAdapter.setData(this.task.getTags());
        employeeAdapter.setData(this.task.getUsers());
        linearLayoutCompleted.setSelected(task.getCompleted());
        if (task.getUsers().isEmpty() || !recommendedUsers.isEmpty()) recommendUsers(task);
    }
    private void recommendUsers(Task task) {
        constraintLayoutRecommendation.setVisibility(View.VISIBLE);
        if (!recommendedUsers.isEmpty()) return;
        ActivityUtils.showProgressBar(progressBarRecommendation);
        raiseUpAPI.proposeUsers(UserUtils.loadBearerToken(this),task.getId()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(TaskActivity.this,response);
                    ActivityUtils.showToast(TaskActivity.this,t,msg);
                }else {
                    recommendedUsers.clear();
                    JsonArray jsonArray = response.body().get("data").getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        recommendedUsers.add(User.buildFromJSON(jsonArray.get(i).getAsJsonObject()));
                    }
                    if (recommendedUsers.isEmpty()){
                        constraintLayoutRecommendation.setVisibility(View.GONE);
                    }else {
                        recommendedEmployeeAdapter.notifyDataSetChanged();
                    }
                }
                ActivityUtils.hideProgressBar(progressBarRecommendation);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(TaskActivity.this,t);
                ActivityUtils.hideProgressBar(progressBarRecommendation);
            }
        });
    }

    private void removeTag(int position) {
        List<Long> tagIds = new ArrayList<>();
        Long deletedTagId = task.getTags().get(position).getId();
        task.getTags().stream().filter(tag -> !Objects.equals(tag.getId(), deletedTagId)).allMatch(tag -> tagIds.add(tag.getId()));
        updateTask(new TaskRequest.Builder().setTagIds(tagIds).build());
    }
    private void removeEmployee(int position) {
        List<Long> userIds = new ArrayList<>();
        Long deletedUserId = task.getUsers().get(position).getId();
        task.getUsers().stream().filter(user -> !Objects.equals(user.getId(), deletedUserId)).allMatch(tag -> userIds.add(tag.getId()));
        updateTask(new TaskRequest.Builder().setEmployeeIds(userIds).build());
    }
    private void addEmployees(View view) {
        CustomBottomSheet.addEmployees(TaskActivity.this, task.getUsers(),task.getColumn().getBoardId(), false, TaskActivity.this::updateEmployees);
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
package com.unipi.msc.raiseupandroid.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.unipi.msc.raiseupandroid.Retrofit.RetrofitClient;
import com.unipi.msc.raiseupandroid.Tools.ActivityUtils;
import com.unipi.msc.raiseupandroid.Tools.CustomBottomSheet;
import com.unipi.msc.raiseupandroid.Tools.NameTag;
import com.unipi.msc.raiseupandroid.Tools.RecyclerViewUtils;
import com.unipi.msc.raiseupandroid.Tools.RetrofitUtils;
import com.unipi.msc.raiseupandroid.Tools.UserUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskActivity extends AppCompatActivity {
    LinearLayout linearLayoutColumnChange;
    TextView textViewDaysToExpire, textViewColumnName,
             textViewFirstName, textViewTaskTitle, textViewDescription;
    RecyclerView recyclerViewTags, recyclerViewEmployees,
                 recyclerViewRecommendedEmployees;
    Task task;
    TagAdapter tagAdapter;

    ImageButton imageButtonExit, imageButtonAddEmployees;
    RaiseUpAPI raiseUpAPI;
    Toast t;
    EmployeeAdapter employeeAdapter;
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

    private void initObjects() {
        task = new Task();
        tagAdapter = new TagAdapter(this,task.getTags());
        employeeAdapter = new EmployeeAdapter(this,task.getUsers(),(view, position) -> removeEmployee(position));
        raiseUpAPI = RetrofitClient.getInstance(this).create(RaiseUpAPI.class);
    }

    private void removeEmployee(int position) {
    }

    private void loadData(Task task) {
        this.task = task;
        textViewTaskTitle.setText(task.getTitle());
        textViewColumnName.setText(task.getColumn().getTitle());
        textViewDescription.setText(task.getDsc());
        tagAdapter.setData(this.task.getTags());
        employeeAdapter.setData(this.task.getUsers());
    }
    private void initListeners() {
        imageButtonExit.setOnClickListener(view->finish());
        imageButtonAddEmployees.setOnClickListener(this::addEmployees);
        recyclerViewTags.setLayoutManager(RecyclerViewUtils.getFlexLayout(this));
        recyclerViewTags.setAdapter(tagAdapter);
        recyclerViewEmployees.setLayoutManager(RecyclerViewUtils.getFlexLayout(this));
        recyclerViewEmployees.setAdapter(employeeAdapter);
    }

    private void addEmployees(View view) {
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
                    CustomBottomSheet.addEmployees(TaskActivity.this, users, employees -> employeeAdapter.setData(employees));
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(TaskActivity.this, t);
            }
        });
    }

    private void getTask(Long taskId) {
        if (taskId == 0L) return;
        raiseUpAPI.getTask(UserUtils.loadBearerToken(this),taskId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(TaskActivity.this,response);
                    ActivityUtils.showToast(TaskActivity.this, t, msg);
                }else{
                    loadData(Task.buildTaskFromJSON(response.body().get("data").getAsJsonObject()));
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(TaskActivity.this, t);
            }
        });
    };

    private void initViews() {
        imageButtonExit = findViewById(R.id.imageButtonExit);
        linearLayoutColumnChange = findViewById(R.id.linearLayoutColumnChange);
        textViewDaysToExpire = findViewById(R.id.textViewDaysToExpire);
        textViewColumnName = findViewById(R.id.textViewColumnName);
        textViewFirstName = findViewById(R.id.textViewFirstName);
        textViewTaskTitle = findViewById(R.id.textViewTaskTitle);
        textViewDescription = findViewById(R.id.textViewDescription);
        recyclerViewTags = findViewById(R.id.recyclerViewTags);
        recyclerViewEmployees = findViewById(R.id.recyclerViewEmployees);
        recyclerViewRecommendedEmployees = findViewById(R.id.recyclerViewRecommendedEmployees);
        imageButtonAddEmployees = findViewById(R.id.imageButtonAddEmployees);
    }
}
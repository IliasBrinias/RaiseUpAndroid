package com.unipi.msc.raiseupandroid.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.JsonObject;
import com.unipi.msc.raiseupandroid.Adapter.ColumnAdapter;
import com.unipi.msc.raiseupandroid.Interface.OnColumnTaskListener;
import com.unipi.msc.raiseupandroid.Model.Board;
import com.unipi.msc.raiseupandroid.Model.Column;
import com.unipi.msc.raiseupandroid.Model.Task;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.raiseupandroid.Retrofit.Request.TaskRequest;
import com.unipi.msc.raiseupandroid.Retrofit.RetrofitClient;
import com.unipi.msc.raiseupandroid.Tools.ActivityUtils;
import com.unipi.msc.raiseupandroid.Tools.CustomBottomSheet;
import com.unipi.msc.raiseupandroid.Tools.MockData;
import com.unipi.msc.raiseupandroid.Tools.NameTag;
import com.unipi.msc.raiseupandroid.Tools.RetrofitUtils;
import com.unipi.msc.raiseupandroid.Tools.UserUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardActivity extends AppCompatActivity{
    private RecyclerView recyclerViewColumns;
    private TextView textViewBoardTitle;
    private ImageButton imageButtonExit;
    private Board board;
    private LinearProgressIndicator linearProgressIndicator;
    private RaiseUpAPI raiseUpAPI;
    private ColumnAdapter columnAdapter;
    private ProgressBar progressBar;
    private Toast t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        initViews();
        initObjects();
        initListeners();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (columnAdapter!=null){
            getBoard(getIntent().getLongExtra(NameTag.BOARD_ID,0L));
        }
    }
    private void initViews() {
        recyclerViewColumns = findViewById(R.id.recyclerViewColumns);
        textViewBoardTitle = findViewById(R.id.textViewBoardTitle);
        imageButtonExit = findViewById(R.id.imageButtonExit);
        linearProgressIndicator = findViewById(R.id.linearProgressIndicator);
        progressBar = findViewById(R.id.progressBar);
    }
    private void initObjects() {
        board = new Board();
        raiseUpAPI = RetrofitClient.getInstance(this).create(RaiseUpAPI.class);
        columnAdapter = new ColumnAdapter(this, board.getColumns(), new OnColumnTaskListener() {
            @Override
            public void onClick(View view, int columnPosition, int taskPosition) {
                Intent intent = new Intent(BoardActivity.this, TaskActivity.class);
                intent.putExtra(NameTag.TASK_ID, board.getColumns().get(columnPosition).getTasks().get(taskPosition).getId());
                BoardActivity.this.startActivity(intent);
            }
            @Override
            public void onTaskCreate(View view, int columnPosition) {
                CustomBottomSheet.AddTaskName(BoardActivity.this,taskName -> {
                    TaskRequest request = new TaskRequest.Builder()
                        .setColumnId(board.getColumns().get(columnPosition).getId())
                        .setTitle(taskName)
                        .build();
                    raiseUpAPI.createTask(UserUtils.loadBearerToken(BoardActivity.this),request).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (!response.isSuccessful()){
                                String msg = RetrofitUtils.handleErrorResponse(BoardActivity.this,response);
                                ActivityUtils.showToast(BoardActivity.this, t, msg);
                            }else{
                                columnAdapter.addTask(columnPosition, Task.buildTaskFromJSON(response.body().get("data").getAsJsonObject()));
                            }
                        }
                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            RetrofitUtils.handleException(BoardActivity.this, t);
                            ActivityUtils.hideProgressBar(progressBar);
                        }
                    });
                });
            }

            @Override
            public void onDelete(View view, int columnPosition, int taskPosition) {
                Long taskId = board.getColumns().get(columnPosition).getTasks().get(taskPosition).getId();
                CustomBottomSheet.deleteMessage(BoardActivity.this,()-> {
                    ActivityUtils.showProgressBar(progressBar);
                    raiseUpAPI.deleteTask(UserUtils.loadBearerToken(BoardActivity.this),taskId).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (!response.isSuccessful()){
                                String msg = RetrofitUtils.handleErrorResponse(BoardActivity.this,response);
                                ActivityUtils.showToast(BoardActivity.this, t, msg);
                            }else{
                                columnAdapter.deleteTask(columnPosition,taskPosition);
                            }
                            ActivityUtils.hideProgressBar(progressBar);
                        }
                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            RetrofitUtils.handleException(BoardActivity.this, t);
                            ActivityUtils.hideProgressBar(progressBar);
                        }
                    });
                });
            }

            @Override
            public void onTaskChangeColumn(View view, int columnPosition, int taskPosition, int targetColumnPosition) {
                ActivityUtils.showProgressBar(progressBar);
                Task task = board.getColumns().get(columnPosition).getTasks().get(taskPosition);
                TaskRequest request = new TaskRequest.Builder().setColumnId(board.getColumns().get(targetColumnPosition).getId()).build();
                raiseUpAPI.updateTask(UserUtils.loadBearerToken(BoardActivity.this),task.getId(),request).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (!response.isSuccessful()){
                            String msg = RetrofitUtils.handleErrorResponse(BoardActivity.this,response);
                            ActivityUtils.showToast(BoardActivity.this, t, msg);
                        }else{
                            columnAdapter.deleteTask(columnPosition, taskPosition);
                            columnAdapter.addTask(targetColumnPosition, Task.buildTaskFromJSON(response.body().get("data").getAsJsonObject()));
                        }
                        ActivityUtils.hideProgressBar(progressBar);
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        RetrofitUtils.handleException(BoardActivity.this, t);
                        ActivityUtils.hideProgressBar(progressBar);
                    }
                });
            }
        });
    }
    private void initListeners() {
        imageButtonExit.setOnClickListener(v->finish());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewColumns.setLayoutManager(linearLayoutManager);
        recyclerViewColumns.setHasFixedSize(true);
        List<Column> columnList = MockData.getTestColumns();
        recyclerViewColumns.setAdapter(columnAdapter);
        recyclerViewColumns.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager myLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                float scrollPosition = myLayoutManager.findFirstVisibleItemPosition() + 1;
                linearProgressIndicator.setProgress((int) ((scrollPosition/columnList.size())*100),true);
            }
        });
    }
    private void getBoard(long boardId) {
        if (boardId == 0L) return;
        ActivityUtils.showProgressBar(progressBar);
        raiseUpAPI.getBoard(UserUtils.loadBearerToken(this), boardId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(BoardActivity.this,response);
                    ActivityUtils.showToast(BoardActivity.this, t, msg);
                }else{
                    board = Board.buildBoardFromJson(response.body().get("data").getAsJsonObject());
                    loadData(board);
                }
                ActivityUtils.hideProgressBar(progressBar);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(BoardActivity.this, t);
                ActivityUtils.hideProgressBar(progressBar);
            }
        });
    }
    private void loadData(Board board) {
        textViewBoardTitle.setText(board.getTitle());
        columnAdapter.setData(board.getColumns());
    }
}
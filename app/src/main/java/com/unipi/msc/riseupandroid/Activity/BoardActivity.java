package com.unipi.msc.riseupandroid.Activity;

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
import com.unipi.msc.riseupandroid.Adapter.ColumnAdapter;
import com.unipi.msc.riseupandroid.Interface.OnColumnTaskListener;
import com.unipi.msc.riseupandroid.Model.Board;
import com.unipi.msc.riseupandroid.Model.Column;
import com.unipi.msc.riseupandroid.Model.Task;
import com.unipi.msc.riseupandroid.Model.User;
import com.unipi.msc.riseupandroid.R;
import com.unipi.msc.riseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.riseupandroid.Retrofit.Request.BoardRequest;
import com.unipi.msc.riseupandroid.Retrofit.Request.StepRequest;
import com.unipi.msc.riseupandroid.Retrofit.Request.TaskRequest;
import com.unipi.msc.riseupandroid.Retrofit.RetrofitClient;
import com.unipi.msc.riseupandroid.Tools.ActivityUtils;
import com.unipi.msc.riseupandroid.Tools.CustomBottomSheet;
import com.unipi.msc.riseupandroid.Tools.MockData;
import com.unipi.msc.riseupandroid.Tools.NameTag;
import com.unipi.msc.riseupandroid.Tools.RetrofitUtils;
import com.unipi.msc.riseupandroid.Tools.UserUtils;

import java.util.List;
import java.util.Objects;

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
    private User user;
    Callback<JsonObject> response;
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
        getBoard(getIntent().getLongExtra(NameTag.BOARD_ID,0L));
        super.onResume();
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
        user = UserUtils.loadUser(this);
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
            public void onColumnTitleClick(View view, int position) {
                if (!Objects.equals(board.getOwnerId(), user.getId())) return;
                CustomBottomSheet.showEdit(BoardActivity.this,getString(R.string.progress_column), board.getColumns().get(position).getTitle(),value -> updateStep(position, new StepRequest.Builder().setTitle(value).build()));
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
            public void onAddColumn(View view) {
                CustomBottomSheet.AddBoardColumn(BoardActivity.this,BoardActivity.this::addColumn);
            }

            @Override
            public void onDeleteColumn(View view, int columnPosition) {
                CustomBottomSheet.deleteMessage(BoardActivity.this,() -> {
                    ActivityUtils.showProgressBar(progressBar);
                    raiseUpAPI.deleteColumn(UserUtils.loadBearerToken(BoardActivity.this),board.getColumns().get(columnPosition).getId()).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (!response.isSuccessful()){
                                String msg = RetrofitUtils.handleErrorResponse(BoardActivity.this,response);
                                ActivityUtils.showToast(BoardActivity.this, t, msg);
                            }else{
                                columnAdapter.deleteColumn(columnPosition);
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
        response = new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(BoardActivity.this,response);
                    ActivityUtils.showToast(BoardActivity.this, t, msg);
                }else{
                    loadData(Board.buildBoardFromJson(response.body().get("data").getAsJsonObject()));
                }
                ActivityUtils.hideProgressBar(progressBar);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(BoardActivity.this, t);
                ActivityUtils.hideProgressBar(progressBar);
            }
        };
    }
    private void initListeners() {
        imageButtonExit.setOnClickListener(v->finish());
        textViewBoardTitle.setOnClickListener(view-> {
            if (!Objects.equals(user.getId(), board.getOwnerId())) return;
            CustomBottomSheet.showEdit(BoardActivity.this,getString(R.string.board_name),textViewBoardTitle.getText().toString(), value -> updateBoard(new BoardRequest.Builder().setTitle(value).build()));
        });
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
        if (boardId == 0L) {
            finish();
            return;
        }
        ActivityUtils.showProgressBar(progressBar);
        raiseUpAPI.getBoard(UserUtils.loadBearerToken(this), boardId).enqueue(response);
    }
    private void addColumn(String name) {
        ActivityUtils.showProgressBar(progressBar);
        raiseUpAPI.addColumn(UserUtils.loadBearerToken(this),board.getId(),new StepRequest.Builder().setTitle(name).build()).enqueue(response);
    }
    private void updateStep(int position, StepRequest request) {
        ActivityUtils.showProgressBar(progressBar);
        raiseUpAPI.editColumn(UserUtils.loadBearerToken(this), board.getColumns().get(position).getId(),request).enqueue(response);
    }
    private void updateBoard(BoardRequest request) {
        ActivityUtils.showProgressBar(progressBar);
        raiseUpAPI.updateBoard(UserUtils.loadBearerToken(BoardActivity.this), board.getId(), request).enqueue(response);
    }
    private void loadData(Board board) {
        this.board = board;
        textViewBoardTitle.setText(board.getTitle());
        columnAdapter.setData(user.getId() == board.getOwnerId(),board.getColumns());
    }
}
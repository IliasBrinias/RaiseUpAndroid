package com.unipi.msc.raiseupandroid.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.JsonObject;
import com.unipi.msc.raiseupandroid.Adapter.ColumnAdapter;
import com.unipi.msc.raiseupandroid.Model.Board;
import com.unipi.msc.raiseupandroid.Model.Column;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.raiseupandroid.Retrofit.RetrofitClient;
import com.unipi.msc.raiseupandroid.Tools.ActivityUtils;
import com.unipi.msc.raiseupandroid.Tools.MockData;
import com.unipi.msc.raiseupandroid.Tools.NameTag;
import com.unipi.msc.raiseupandroid.Tools.RetrofitUtils;
import com.unipi.msc.raiseupandroid.Tools.UserUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardActivity extends AppCompatActivity {
    private RecyclerView recyclerViewColumns;
    private TextView textViewBoardTitle;
    private ImageButton imageButtonExit;
    private Board board;
    private LinearProgressIndicator linearProgressIndicator;
    private RaiseUpAPI raiseUpAPI;
    private ColumnAdapter columnAdapter;
    Toast t;
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

    private void initObjects() {
        board = new Board();
        raiseUpAPI = RetrofitClient.getInstance(this).create(RaiseUpAPI.class);
        columnAdapter = new ColumnAdapter(this,board.getColumns());
    }

    private void getBoard(long boardId) {
        if (boardId == 0L) return;
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
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(BoardActivity.this, t);
            }
        });
    }

    private void loadData(Board board) {
        textViewBoardTitle.setText(board.getTitle());
        columnAdapter.setData(board.getColumns());
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

    private void initViews() {
        recyclerViewColumns = findViewById(R.id.recyclerViewColumns);
        textViewBoardTitle = findViewById(R.id.textViewBoardTitle);
        imageButtonExit = findViewById(R.id.imageButtonExit);
        linearProgressIndicator = findViewById(R.id.linearProgressIndicator);
    }
}
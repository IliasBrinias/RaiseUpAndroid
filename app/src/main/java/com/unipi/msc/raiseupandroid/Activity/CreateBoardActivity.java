package com.unipi.msc.raiseupandroid.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.unipi.msc.raiseupandroid.Adapter.BoardCreationColumnAdapter;
import com.unipi.msc.raiseupandroid.Adapter.EmployeeAdapter;
import com.unipi.msc.raiseupandroid.Interface.OnBoardColumnClick;
import com.unipi.msc.raiseupandroid.Model.Board;
import com.unipi.msc.raiseupandroid.Model.User;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.raiseupandroid.Retrofit.Request.BoardRequest;
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

public class CreateBoardActivity extends AppCompatActivity {
    ImageButton imageButtonClose, imageButtonAddEmployees, imageButtonAddColumn;
    EditText editTextBoardName;
    RecyclerView recyclerViewEmployees, recyclerViewColumns;
    Button buttonCreate;
    Board board;
    List<User> users = new ArrayList<>();
    List<String> columns = new ArrayList<>();
    EmployeeAdapter employeeAdapter;
    BoardCreationColumnAdapter boardCreationColumnAdapter;
    RaiseUpAPI raiseUpAPI;
    Toast t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);
        initView();
        initObjects();
        initListeners();
        loadBoard(getIntent().getLongExtra(NameTag.BOARD_ID,0L));
    }


    private void loadBoard(long longExtra) {
    }


    private void saveBoard(View view) {
        List<Long> userIds = new ArrayList<>();
        users.forEach(user -> userIds.add(user.getId()));
        raiseUpAPI.createBoard(UserUtils.loadBearerToken(this), new BoardRequest(
                editTextBoardName.getText().toString(),
                userIds,
                columns)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(CreateBoardActivity.this,response);
                    ActivityUtils.showToast(CreateBoardActivity.this,t,msg);
                }else{
                    ActivityUtils.showToast(CreateBoardActivity.this,t,getString(R.string.board_created));
                    finish();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(CreateBoardActivity.this,t);
            }
        });
    }

    private void addColumn(View view) {
        CustomBottomSheet.AddBoardColumn(this,column -> boardCreationColumnAdapter.addData(column));
    }

    private void addEmployees(View view) {
        CustomBottomSheet.addEmployees(this, users, employees -> employeeAdapter.setData(employees));
    }

    private void initObjects() {
        raiseUpAPI = RetrofitClient.getInstance(this).create(RaiseUpAPI.class);
        employeeAdapter = new EmployeeAdapter(this,users,(view, position) -> employeeAdapter.deleteItem(users.get(position)));
        boardCreationColumnAdapter = new BoardCreationColumnAdapter(this, columns, new OnBoardColumnClick() {
            @Override
            public void onClick(View view, int position) {
                CustomBottomSheet.showEdit(CreateBoardActivity.this,"'"+columns.get(position)+"'",columns.get(position),value -> boardCreationColumnAdapter.editValue(position, value));
            }

            @Override
            public void onDelete(View view, int position) {
                boardCreationColumnAdapter.deleteItem(columns.get(position));
            }
        });
    }

    private void initListeners() {
        imageButtonClose.setOnClickListener(view->finish());
        imageButtonAddEmployees.setOnClickListener(this::addEmployees);
        imageButtonAddColumn.setOnClickListener(this::addColumn);
        buttonCreate.setOnClickListener(this::saveBoard);
        recyclerViewEmployees.setLayoutManager(RecyclerViewUtils.getFlexLayout(this));
        recyclerViewColumns.setLayoutManager(RecyclerViewUtils.getFlexLayout(this));
        recyclerViewEmployees.setAdapter(employeeAdapter);
        recyclerViewColumns.setAdapter(boardCreationColumnAdapter);
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
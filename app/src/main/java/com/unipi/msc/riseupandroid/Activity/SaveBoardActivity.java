package com.unipi.msc.riseupandroid.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.unipi.msc.riseupandroid.Adapter.BoardCreationColumnAdapter;
import com.unipi.msc.riseupandroid.Adapter.EmployeeAdapter;
import com.unipi.msc.riseupandroid.Interface.OnBoardColumnClick;
import com.unipi.msc.riseupandroid.Model.Column;
import com.unipi.msc.riseupandroid.Model.User;
import com.unipi.msc.riseupandroid.R;
import com.unipi.msc.riseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.riseupandroid.Retrofit.Request.BoardRequest;
import com.unipi.msc.riseupandroid.Retrofit.RetrofitClient;
import com.unipi.msc.riseupandroid.Tools.ActivityUtils;
import com.unipi.msc.riseupandroid.Tools.CustomBottomSheet;
import com.unipi.msc.riseupandroid.Tools.RecyclerViewUtils;
import com.unipi.msc.riseupandroid.Tools.RetrofitUtils;
import com.unipi.msc.riseupandroid.Tools.UserUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveBoardActivity extends AppCompatActivity {
    private ImageButton imageButtonClose, imageButtonAddEmployees, imageButtonAddColumn;
    private EditText editTextBoardName;
    private RecyclerView recyclerViewEmployees, recyclerViewColumns;
    private Button buttonCreate;
    private List<User> users = new ArrayList<>();
    private List<Column> columns = new ArrayList<>();
    private EmployeeAdapter employeeAdapter;
    private BoardCreationColumnAdapter columnAdapter;
    private RaiseUpAPI raiseUpAPI;
    private ItemTouchHelper itemTouchHelper;
    private Toast t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);
        initView();
        initObjects();
        initListeners();
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
    private void initObjects() {
        raiseUpAPI = RetrofitClient.getInstance(this).create(RaiseUpAPI.class);
        employeeAdapter = new EmployeeAdapter(this,users,(view, position) -> employeeAdapter.deleteItem(users.get(position)));
        columnAdapter = new BoardCreationColumnAdapter(this, columns, new OnBoardColumnClick() {
            @Override
            public void onClick(View view, int position) {
                CustomBottomSheet.showEdit(SaveBoardActivity.this,"'"+columns.get(position)+"'",columns.get(position).getTitle(), value -> columnAdapter.editValue(position, value));
            }
            @Override
            public void onDelete(View view, int position) {
                columnAdapter.deleteItem(position);
            }
        });
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN|ItemTouchHelper.UP,0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Collections.swap(columns,viewHolder.getAdapterPosition(),target.getAdapterPosition());
                columnAdapter.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
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
        recyclerViewColumns.setAdapter(columnAdapter);
        itemTouchHelper.attachToRecyclerView(recyclerViewColumns);
    }
    private void addColumn(View view) {
        CustomBottomSheet.AddBoardColumn(this, columnName -> columnAdapter.addData(Column.getInstance(columnName)));
    }
    private void addEmployees(View view) {
        CustomBottomSheet.addEmployees(this, users, 0L, true,employees -> employeeAdapter.setData(employees));
    }
    private void saveBoard(View view) {
        List<Long> userIds = new ArrayList<>();
        List<String> columnNames = new ArrayList<>();

        users.forEach(user -> userIds.add(user.getId()));
        columns.forEach(column->columnNames.add(column.getTitle()));
        BoardRequest request = new BoardRequest.Builder().setTitle(editTextBoardName.getText().toString()).setEmployeesId(userIds).setColumns(columnNames).build();
        raiseUpAPI.createBoard(UserUtils.loadBearerToken(this),request).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(SaveBoardActivity.this,response);
                    ActivityUtils.showToast(SaveBoardActivity.this,t,msg);
                }else{
                    setResult(RESULT_OK);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(SaveBoardActivity.this,t);
            }
        });
    }
}
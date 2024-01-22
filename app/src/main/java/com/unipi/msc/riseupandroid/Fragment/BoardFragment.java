package com.unipi.msc.riseupandroid.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unipi.msc.riseupandroid.Activity.BoardActivity;
import com.unipi.msc.riseupandroid.Activity.SaveBoardActivity;
import com.unipi.msc.riseupandroid.Adapter.BoardAdapter;
import com.unipi.msc.riseupandroid.Interface.OnBoardClick;
import com.unipi.msc.riseupandroid.Model.Board;
import com.unipi.msc.riseupandroid.Model.Column;
import com.unipi.msc.riseupandroid.R;
import com.unipi.msc.riseupandroid.Retrofit.ColumnRequest;
import com.unipi.msc.riseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.riseupandroid.Retrofit.Request.BoardRequest;
import com.unipi.msc.riseupandroid.Retrofit.RetrofitClient;
import com.unipi.msc.riseupandroid.Tools.ActivityUtils;
import com.unipi.msc.riseupandroid.Tools.CustomBottomSheet;
import com.unipi.msc.riseupandroid.Tools.ItemViewModel;
import com.unipi.msc.riseupandroid.Tools.NameTag;
import com.unipi.msc.riseupandroid.Tools.RetrofitUtils;
import com.unipi.msc.riseupandroid.Tools.UserUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardFragment extends Fragment {
    private RecyclerView recyclerView;
    private ImageButton imageButtonCreateBoard;
    private BoardAdapter boardAdapter;
    private ProgressBar progressBar;
    private RaiseUpAPI raiseUpAPI;
    private Toast t;
    private List<Board> boardList = new ArrayList<>();
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            CustomBottomSheet.successMessage(requireActivity(),getString(R.string.board));
        }
    });
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_board, container, false);
        initViews(view);
        initObjects();
        initListeners();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        imageButtonCreateBoard = view.findViewById(R.id.imageButtonCreateBoard);
        progressBar = view.findViewById(R.id.progressBar);
    }
    private void initObjects() {
        raiseUpAPI = RetrofitClient.getInstance(requireActivity()).create(RaiseUpAPI.class);
        boardAdapter = new BoardAdapter(requireActivity(), boardList, new OnBoardClick() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(requireActivity(), BoardActivity.class);
                intent.putExtra(NameTag.BOARD_ID, boardList.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void addEmployees(View view, int position) {
                BoardFragment.this.addEmployees(position);
            }

            @Override
            public boolean onLongClick(View view, int position) {
                if (!Objects.equals(boardList.get(position).getOwner().getId(), UserUtils.loadUser(getActivity()).getId())) return false;
                CustomBottomSheet.getBoardProperties(requireActivity(), choice -> {
                    if (choice == CustomBottomSheet.ADD_EMPLOYEES) {
                        BoardFragment.this.addEmployees(position);
                    } else if (choice == CustomBottomSheet.CHANGE_ORDER) {
                        CustomBottomSheet.changeColumnOrder(requireActivity(),boardList.get(position).getId(), columns ->  updateColumnOrder(boardList.get(position).getId(), columns));
                    } else if (choice == CustomBottomSheet.DELETE_BOARD) {
                        deleteBoard(position);
                    }
                });
                return false;
            }
        });
    }

    private void updateColumnOrder(Long boardId, List<Column> columns) {
        List<ColumnRequest> columnRequests = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            columnRequests.add(new ColumnRequest(columns.get(i).getId(), (long) i));
        }
        raiseUpAPI.updateColumnOrder(UserUtils.loadBearerToken(requireActivity()), boardId, columnRequests).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(requireActivity(),response);
                    ActivityUtils.showToast(requireActivity(), t, msg);
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(requireActivity(), t);
            }
        });
    }

    private void addEmployees(int position) {
        CustomBottomSheet.addEmployees(requireActivity(),boardList.get(position).getUsers(),boardList.get(position).getId(), true, employees -> {
            List<Long> employeeIds = new ArrayList<>();
            employees.forEach(user -> employeeIds.add(user.getId()));
            updateBoard(position, new BoardRequest.Builder().setEmployeesId(employeeIds).build());
        });
    }

    private void initListeners() {
        imageButtonCreateBoard.setOnClickListener(view -> mStartForResult.launch(new Intent(requireActivity(), SaveBoardActivity.class)));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(boardAdapter);
    }
    private void loadData() {
        Callback<JsonObject> callback = new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(requireActivity(),response);
                    ActivityUtils.showToast(requireActivity(),t,msg);
                }else {
                    boardAdapter.clearData();
                    JsonArray jsonArray = response.body().get("data").getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        boardAdapter.addItem(Board.buildBoardFromJson(jsonArray.get(i).getAsJsonObject()));
                    }
                }
                ActivityUtils.hideProgressBar(progressBar);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(requireActivity(), t);
                ActivityUtils.hideProgressBar(progressBar);
            }
        };
        new ViewModelProvider(requireActivity()).get(ItemViewModel.class).getKeyword().observe(getViewLifecycleOwner(), keyword -> {
            ActivityUtils.showProgressBar(progressBar);
            raiseUpAPI.searchBoard(UserUtils.loadBearerToken(requireActivity()), keyword).enqueue(callback);
        });
        ActivityUtils.showProgressBar(progressBar);
        raiseUpAPI.getBoards(UserUtils.loadBearerToken(requireActivity())).enqueue(callback);
    }
    private void updateBoard(int position,BoardRequest request) {
        ActivityUtils.showProgressBar(progressBar);
        raiseUpAPI.updateBoard(UserUtils.loadBearerToken(requireActivity()), boardList.get(position).getId(), request).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(requireActivity(),response);
                    ActivityUtils.showToast(requireActivity(),t,msg);
                }else {
                    boardAdapter.updateItem(position, Board.buildBoardFromJson(response.body().get("data").getAsJsonObject()));
                }
                ActivityUtils.hideProgressBar(progressBar);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(requireActivity(), t);
                ActivityUtils.hideProgressBar(progressBar);
            }
        });
    }
    private void deleteBoard(int position) {
        ActivityUtils.showProgressBar(progressBar);
        raiseUpAPI.deleteBoard(UserUtils.loadBearerToken(requireActivity()),boardList.get(position).getId()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(requireActivity(),response);
                    ActivityUtils.showToast(requireActivity(),t,msg);
                }else {
                    boardAdapter.removeItem(position);
                }
                ActivityUtils.hideProgressBar(progressBar);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(requireActivity(), t);
                ActivityUtils.hideProgressBar(progressBar);
            }
        });
    }
}
package com.unipi.msc.raiseupandroid.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unipi.msc.raiseupandroid.Activity.BoardActivity;
import com.unipi.msc.raiseupandroid.Activity.CreateBoardActivity;
import com.unipi.msc.raiseupandroid.Adapter.BoardAdapter;
import com.unipi.msc.raiseupandroid.Model.Board;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.raiseupandroid.Retrofit.RetrofitClient;
import com.unipi.msc.raiseupandroid.Tools.ActivityUtils;
import com.unipi.msc.raiseupandroid.Tools.NameTag;
import com.unipi.msc.raiseupandroid.Tools.RetrofitUtils;
import com.unipi.msc.raiseupandroid.Tools.UserUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardFragment extends Fragment {
    RecyclerView recyclerView;
    ImageButton imageButtonCreateBoard;
    BoardAdapter boardAdapter;
    RaiseUpAPI raiseUpAPI;
    Toast t;
    List<Board> boardList = new ArrayList<>();

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
        if (boardAdapter!=null){
            loadData();
        }
        super.onResume();
    }

    private void loadData() {
        raiseUpAPI.getBoards(UserUtils.loadBearerToken(requireActivity())).enqueue(new Callback<JsonObject>() {
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
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(requireActivity(), t);
            }
        });
    }

    private void initObjects() {
        raiseUpAPI = RetrofitClient.getInstance(requireActivity()).create(RaiseUpAPI.class);
        boardAdapter = new BoardAdapter(requireActivity(), boardList, (v, position) -> {
            Intent intent = new Intent(requireActivity(), BoardActivity.class);
            intent.putExtra(NameTag.BOARD_ID,boardList.get(position).getId());
            startActivity(intent);
        });
    }

    private void initListeners() {
        imageButtonCreateBoard.setOnClickListener(view -> startActivity(new Intent(requireActivity(), CreateBoardActivity.class)));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(boardAdapter);
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        imageButtonCreateBoard = view.findViewById(R.id.imageButtonCreateBoard);
    }
}
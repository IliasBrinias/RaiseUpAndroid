package com.unipi.msc.riseupandroid.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unipi.msc.riseupandroid.Activity.TaskActivity;
import com.unipi.msc.riseupandroid.Adapter.TaskAdapter;
import com.unipi.msc.riseupandroid.Model.Task;
import com.unipi.msc.riseupandroid.R;
import com.unipi.msc.riseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.riseupandroid.Retrofit.RetrofitClient;
import com.unipi.msc.riseupandroid.Tools.ActivityUtils;
import com.unipi.msc.riseupandroid.Tools.ItemViewModel;
import com.unipi.msc.riseupandroid.Tools.NameTag;
import com.unipi.msc.riseupandroid.Tools.RetrofitUtils;
import com.unipi.msc.riseupandroid.Tools.UserUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskFragment extends Fragment {
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private ProgressBar progressBar;
    private RaiseUpAPI raiseUpAPI;
    private Toast t;
    private List<Task> taskList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_task, container, false);
        initViews(v);
        initObjects();
        initListener();
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
    private void initViews(View v) {
        recyclerView = v.findViewById(R.id.recyclerView);
        progressBar = v.findViewById(R.id.progressBar);
    }
    private void initObjects() {
        raiseUpAPI = RetrofitClient.getInstance(requireActivity()).create(RaiseUpAPI.class);
        taskAdapter = new TaskAdapter(requireActivity(), taskList, (view, position) -> {
            Intent intent = new Intent(requireActivity(), TaskActivity.class);
            intent.putExtra(NameTag.TASK_ID, taskList.get(position).getId());
            requireActivity().startActivity(intent);
        });
    }
    private void initListener() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(taskAdapter);
    }
    private void loadData() {
        Callback<JsonObject> callback = new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(requireActivity(), response);
                    ActivityUtils.showToast(requireActivity(),t,msg);
                }else{
                    JsonArray jsonArray = response.body().get("data").getAsJsonArray();
                    taskList.clear();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        taskList.add(Task.buildTaskFromJSON(jsonArray.get(i).getAsJsonObject()));
                    }
                    taskAdapter.refreshData();
                }
                ActivityUtils.hideProgressBar(progressBar);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(requireActivity(),t);
                ActivityUtils.hideProgressBar(progressBar);
            }
        };
        new ViewModelProvider(requireActivity()).get(ItemViewModel.class).getKeyword().observe(getViewLifecycleOwner(), keyword -> {
            ActivityUtils.showProgressBar(progressBar);
            raiseUpAPI.searchTasks(UserUtils.loadBearerToken(requireActivity()), keyword).enqueue(callback);
        });
        ActivityUtils.showProgressBar(progressBar);
        raiseUpAPI.getTasks(UserUtils.loadBearerToken(requireActivity())).enqueue(callback);
    }
}
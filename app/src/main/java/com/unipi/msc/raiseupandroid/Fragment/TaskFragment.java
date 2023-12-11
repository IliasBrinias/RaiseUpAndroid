package com.unipi.msc.raiseupandroid.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unipi.msc.raiseupandroid.Activity.TaskActivity;
import com.unipi.msc.raiseupandroid.Adapter.TaskAdapter;
import com.unipi.msc.raiseupandroid.Model.Task;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Tools.MockData;
import com.unipi.msc.raiseupandroid.Tools.NameTag;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {

    RecyclerView recyclerView;
    TaskAdapter taskAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_task, container, false);
        initViews(v);
        initListener();
        return v;
    }

    private void initListener() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        List<Task> taskList = MockData.getTestTasks();
        taskAdapter = new TaskAdapter(requireActivity(), taskList, (view, position) -> {
            Intent intent = new Intent(requireActivity(), TaskActivity.class);
            intent.putExtra(NameTag.TASK_ID,2L);
            startActivity(intent);
        });
        recyclerView.setAdapter(taskAdapter);
    }

    private void initViews(View v) {
        recyclerView = v.findViewById(R.id.recyclerView);
    }
}
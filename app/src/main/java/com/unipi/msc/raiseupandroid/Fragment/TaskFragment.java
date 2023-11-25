package com.unipi.msc.raiseupandroid.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unipi.msc.raiseupandroid.Adapter.TaskAdapter;
import com.unipi.msc.raiseupandroid.Model.Task;
import com.unipi.msc.raiseupandroid.R;

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
        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task(0L,"Test0","bla bla bal bal",12L,new ArrayList<>(), new ArrayList<>()));
        taskList.add(new Task(1L,"Test1","blaasdasdasasdasdasdasd",12L,new ArrayList<>(), new ArrayList<>()));
        taskList.add(new Task(2L,"Test2","bla blaasdasdasasdasdasdasdbal bal",12L,new ArrayList<>(), new ArrayList<>()));
        taskList.add(new Task(3L,"Test3","bla bla bal blaasdasdasasdasdasdasd",12L,new ArrayList<>(), new ArrayList<>()));
        taskAdapter = new TaskAdapter(requireActivity(), taskList, (view, position) -> {

        });
        recyclerView.setAdapter(taskAdapter);
    }

    private void initViews(View v) {
        recyclerView = v.findViewById(R.id.recyclerView);
    }
}
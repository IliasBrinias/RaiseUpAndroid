package com.unipi.msc.raiseupandroid.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.msc.raiseupandroid.Interface.OnTaskClick;
import com.unipi.msc.raiseupandroid.Model.Column;
import com.unipi.msc.raiseupandroid.Model.Task;
import com.unipi.msc.raiseupandroid.R;

import java.util.ArrayList;
import java.util.List;

public class ColumnAdapter extends RecyclerView.Adapter<ColumnAdapter.ColumnViewHolder> {
    Activity a;
    List<Column> columnList;

    public ColumnAdapter(Activity a, List<Column> columnList) {
        this.a = a;
        this.columnList = columnList;
    }

    @NonNull
    @Override
    public ColumnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.column_layout, parent, false);
        return new ColumnViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ColumnViewHolder holder, int position) {
        holder.bindData(a, columnList.get(position));
    }

    @Override
    public int getItemCount() {
        return columnList.size();
    }

    public static class ColumnViewHolder extends RecyclerView.ViewHolder {
        TextView textViewColumnTitle;
        RecyclerView recyclerView;
        TaskAdapter taskAdapter;
        public ColumnViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
            initListeners();
        }

        private void initListeners() {
        }

        private void initViews(View view) {
            textViewColumnTitle = view.findViewById(R.id.textViewColumnTitle);
            recyclerView = view.findViewById(R.id.recyclerViewTask);
        }
        public void bindData(Activity a, Column column){
            textViewColumnTitle.setText(column.getName());
            recyclerView.setLayoutManager(new LinearLayoutManager(a));
            List<Task> taskList = new ArrayList<>();
            taskList.add(new Task(0L,"Test0","bla bla bal bal",12L,new ArrayList<>(), new ArrayList<>()));
            taskList.add(new Task(1L,"Test1","blaasdasdasasdasdasdasd",12L,new ArrayList<>(), new ArrayList<>()));
            taskList.add(new Task(2L,"Test2","bla blaasdasdasasdasdasdasdbal bal",12L,new ArrayList<>(), new ArrayList<>()));
            taskList.add(new Task(3L,"Test3","bla bla bal blaasdasdasasdasdasdasd",12L,new ArrayList<>(), new ArrayList<>()));
            taskAdapter = new TaskAdapter(a, taskList, (view, position) -> {

            });
            recyclerView.setAdapter(taskAdapter);
        }
    }
}

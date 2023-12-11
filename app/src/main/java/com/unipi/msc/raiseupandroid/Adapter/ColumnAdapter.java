package com.unipi.msc.raiseupandroid.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.msc.raiseupandroid.Activity.TaskActivity;
import com.unipi.msc.raiseupandroid.Interface.OnTaskClick;
import com.unipi.msc.raiseupandroid.Model.Column;
import com.unipi.msc.raiseupandroid.Model.Task;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Tools.MockData;
import com.unipi.msc.raiseupandroid.Tools.NameTag;

import java.util.ArrayList;
import java.util.List;

public class ColumnAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int PADDING = 0;
    private static final int COLUMN = 1;
    Activity a;
    List<Column> columnList;


    public ColumnAdapter(Activity a, List<Column> columnList) {
        this.a = a;
        this.columnList = columnList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == PADDING){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_column_start_padding_layout, parent, false);
            return new PaddingViewHolder(v);
        }else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.column_layout, parent, false);
            return new ColumnViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder view, int position) {
        if (view instanceof ColumnViewHolder) {
            ColumnViewHolder holder = (ColumnViewHolder) view;
            Column column = columnList.get(position - 1);
            holder.bindData(a, column);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return PADDING;
        }else{
            return COLUMN;
        }
    }
    @Override
    public int getItemCount() {
        return columnList.size() + 1;
    }

    public static class PaddingViewHolder extends RecyclerView.ViewHolder {
        public PaddingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    public static class ColumnViewHolder extends RecyclerView.ViewHolder {
        TextView textViewColumnTitle;
        RecyclerView recyclerView;
        TaskAdapter taskAdapter;
        LinearLayout linearLayout;
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
            linearLayout = view.findViewById(R.id.linearLayout);
        }
        public void bindData(Activity a, Column column){
            textViewColumnTitle.setText(column.getName());
            recyclerView.setLayoutManager(new LinearLayoutManager(a));
            List<Task> taskList = MockData.getTestTasks();
            taskAdapter = new TaskAdapter(a, taskList, (view, position) -> {
                Intent intent = new Intent(a, TaskActivity.class);
                intent.putExtra(NameTag.TASK_ID,2L);
                a.startActivity(intent);
            });
            recyclerView.setAdapter(taskAdapter);
        }
    }
}

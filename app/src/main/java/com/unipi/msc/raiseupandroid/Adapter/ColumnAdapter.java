package com.unipi.msc.raiseupandroid.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.msc.raiseupandroid.Interface.OnColumnTaskListener;
import com.unipi.msc.raiseupandroid.Interface.OnTaskClick;
import com.unipi.msc.raiseupandroid.Model.Column;
import com.unipi.msc.raiseupandroid.Model.Task;
import com.unipi.msc.raiseupandroid.R;

import java.util.List;

public class ColumnAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int PADDING = 0;
    private static final int COLUMN = 1;
    Activity a;
    List<Column> columnList;
    OnColumnTaskListener onColumnTaskListener;

    public ColumnAdapter(Activity a, List<Column> columnList, OnColumnTaskListener onColumnTaskListener) {
        this.a = a;
        this.columnList = columnList;
        this.onColumnTaskListener = onColumnTaskListener;
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
            holder.bindData(a, position, columnList);
            holder.onColumnTaskListener = onColumnTaskListener;
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

    public void setData(List<Column> columns) {
        this.columnList.clear();
        columnList = columns;
        notifyDataSetChanged();
    }

    public void deleteTask(int columnPosition, int taskPosition) {
        Task task = columnList.get(columnPosition).getTasks().get(taskPosition);
        if (columnList.get(columnPosition).getTasks().remove(task)) notifyItemChanged(columnPosition + 1);
    }

    public void addTask(int columnPosition, Task task) {
        columnList.get(columnPosition).getTasks().add(task);
        notifyItemChanged(columnPosition + 1);
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
        LinearLayout linearLayout, linearLayoutCreateTask;
        List<Task> tasks;
        OnColumnTaskListener onColumnTaskListener;
        public ColumnViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
            initListeners();
        }

        private void initListeners() {
            linearLayoutCreateTask.setOnClickListener(view -> onColumnTaskListener.onTaskCreate(view,getAdapterPosition()-1));
            textViewColumnTitle.setOnClickListener(view -> onColumnTaskListener.onColumnTitleClick(view,getAdapterPosition()-1));
        }

        private void initViews(View view) {
            textViewColumnTitle = view.findViewById(R.id.textViewColumnTitle);
            recyclerView = view.findViewById(R.id.recyclerViewTask);
            linearLayout = view.findViewById(R.id.linearLayout);
            linearLayoutCreateTask = view.findViewById(R.id.linearLayoutCreateTask);
        }
        public void bindData(Activity a, int position, List<Column> columnList){
            Column column = columnList.get(position - 1);
            textViewColumnTitle.setText(column.getTitle());
            recyclerView.setLayoutManager(new LinearLayoutManager(a));
            tasks = column.getTasks();
            taskAdapter = new TaskAdapter(a, tasks, position == 1, position == columnList.size(), new OnTaskClick() {
                @Override
                public void onClick(View view, int position) {
                    onColumnTaskListener.onClick(view, getAdapterPosition()-1, position);
                }
                @Override
                public void onDelete(View view, int position) {
                    onColumnTaskListener.onDelete(view, getAdapterPosition()-1, position);
                }
                @Override
                public void onNextColumn(View view, int position) {
                    onColumnTaskListener.onTaskChangeColumn(view, getAdapterPosition()-1, position, getAdapterPosition());
                }
                @Override
                public void onPreviousColumn(View view, int position) {
                    onColumnTaskListener.onTaskChangeColumn(view, getAdapterPosition()-1, position, getAdapterPosition()-2);
                }
            });
            recyclerView.setAdapter(taskAdapter);
        }
    }
}

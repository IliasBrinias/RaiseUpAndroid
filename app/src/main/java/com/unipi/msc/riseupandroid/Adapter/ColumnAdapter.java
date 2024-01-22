package com.unipi.msc.riseupandroid.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.msc.riseupandroid.Interface.OnColumnTaskListener;
import com.unipi.msc.riseupandroid.Interface.OnTaskClick;
import com.unipi.msc.riseupandroid.Model.Column;
import com.unipi.msc.riseupandroid.Model.Task;
import com.unipi.msc.riseupandroid.R;

import java.util.List;

public class ColumnAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int PADDING = 0;
    private static final int COLUMN = 1;
    private static final int ADD_COLUMN = 2;
    private final Activity a;
    private List<Column> columnList;
    private final OnColumnTaskListener onColumnTaskListener;
    private boolean isOwner;
    public ColumnAdapter(Activity a, List<Column> columnList, OnColumnTaskListener onColumnTaskListener) {
        this.a = a;
        this.columnList = columnList;
        this.onColumnTaskListener = onColumnTaskListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        switch (viewType){
            case PADDING:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_column_start_padding_layout, parent, false);
                return new PaddingViewHolder(v);
            case COLUMN:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.column_layout, parent, false);
                return new ColumnViewHolder(v);
            case ADD_COLUMN:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_column_layout, parent, false);
                return new AddColumnViewHolder(v);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder view, int position) {
        if (view instanceof ColumnViewHolder) {
            ColumnViewHolder holder = (ColumnViewHolder) view;
            holder.bindData(a, isOwner, position, columnList);
            holder.onColumnTaskListener = onColumnTaskListener;
        } else if (view instanceof AddColumnViewHolder) {
            AddColumnViewHolder holder = (AddColumnViewHolder) view;
            holder.setOnColumnTaskListener(onColumnTaskListener);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return PADDING;
        }else if(position == columnList.size() + 1 && isOwner) {
            return ADD_COLUMN;
        }else{
            return COLUMN;
        }
    }
    @Override
    public int getItemCount() {
        return isOwner ? columnList.size() + 2 : columnList.size() + 1;
    }
    public void setData(boolean isOwner, List<Column> columns) {
        this.isOwner = isOwner;
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
    public void deleteColumn(int columnPosition) {
        columnList.remove(columnPosition);
        notifyItemRemoved(columnPosition + 1);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public static class PaddingViewHolder extends RecyclerView.ViewHolder {
        public PaddingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    public static class ColumnViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewColumnDelete;
        private TextView textViewColumnTitle;
        private RecyclerView recyclerView;
        private LinearLayout linearLayoutCreateTask;
        private OnColumnTaskListener onColumnTaskListener;
        public ColumnViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
            initListeners();
        }
        private void initViews(View view) {
            textViewColumnTitle = view.findViewById(R.id.textViewColumnTitle);
            recyclerView = view.findViewById(R.id.recyclerViewTask);
            linearLayoutCreateTask = view.findViewById(R.id.linearLayoutCreateTask);
            imageViewColumnDelete = view.findViewById(R.id.imageViewColumnDelete);
        }
        private void initListeners() {
            linearLayoutCreateTask.setOnClickListener(view -> onColumnTaskListener.onTaskCreate(view,getAdapterPosition()-1));
            textViewColumnTitle.setOnClickListener(view -> onColumnTaskListener.onColumnTitleClick(view,getAdapterPosition()-1));
            imageViewColumnDelete.setOnClickListener(view -> onColumnTaskListener.onDeleteColumn(view, getAdapterPosition()-1));
        }
        public void bindData(Activity a, boolean isOwner, int position, List<Column> columnList){
            Column column = columnList.get(position - 1);
            TaskAdapter taskAdapter = new TaskAdapter(a, column.getTasks(), position == 1, position == columnList.size(), new OnTaskClick() {
                @Override
                public void onClick(View view, int position) {
                    onColumnTaskListener.onClick(view, getAdapterPosition() - 1, position);
                }

                @Override
                public void onDelete(View view, int position) {
                    onColumnTaskListener.onDelete(view, getAdapterPosition() - 1, position);
                }

                @Override
                public void onNextColumn(View view, int position) {
                    onColumnTaskListener.onTaskChangeColumn(view, getAdapterPosition() - 1, position, getAdapterPosition());
                }

                @Override
                public void onPreviousColumn(View view, int position) {
                    onColumnTaskListener.onTaskChangeColumn(view, getAdapterPosition() - 1, position, getAdapterPosition() - 2);
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(a));
            recyclerView.setAdapter(taskAdapter);
            textViewColumnTitle.setText(column.getTitle());
            imageViewColumnDelete.setVisibility(isOwner?View.VISIBLE:View.GONE);
        }
    }
    private static class AddColumnViewHolder extends RecyclerView.ViewHolder {
        private OnColumnTaskListener onColumnTaskListener;
        public AddColumnViewHolder(View v) {
            super(v);
            v.setOnClickListener(view->{
                if (onColumnTaskListener == null) return;
                onColumnTaskListener.onAddColumn(view);
            });
        }
        public void setOnColumnTaskListener(OnColumnTaskListener onColumnTaskListener) {
            this.onColumnTaskListener = onColumnTaskListener;
        }
    }
}

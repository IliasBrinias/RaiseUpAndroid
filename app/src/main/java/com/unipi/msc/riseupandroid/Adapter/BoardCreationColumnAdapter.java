package com.unipi.msc.riseupandroid.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.msc.riseupandroid.Interface.OnBoardColumnClick;
import com.unipi.msc.riseupandroid.Model.Column;
import com.unipi.msc.riseupandroid.R;

import java.util.List;

public class BoardCreationColumnAdapter extends RecyclerView.Adapter<BoardCreationColumnAdapter.ColumnViewHolder> {
    Activity a;
    List<Column> columns;
    OnBoardColumnClick onBoardColumnClick;

    public BoardCreationColumnAdapter(Activity a, List<Column> columns, OnBoardColumnClick onBoardColumnClick) {
        this.a = a;
        this.columns = columns;
        this.onBoardColumnClick = onBoardColumnClick;
    }

    @NonNull
    @Override
    public BoardCreationColumnAdapter.ColumnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_creation_column_layout, parent, false);
        return new BoardCreationColumnAdapter.ColumnViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardCreationColumnAdapter.ColumnViewHolder holder, int position) {
        if (onBoardColumnClick == null){
            holder.imageButtonDelete.setVisibility(View.GONE);
        }else {
            holder.onBoardColumnClick = onBoardColumnClick;
        }

        holder.textViewName.setText(columns.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return columns.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public void deleteItem(int position) {
        columns.remove(position);
        notifyItemRemoved(position);
    }

    public void addData(Column column) {
        columns.add(column);
        notifyDataSetChanged();
    }

    public void editValue(int position, String value) {
        columns.get(position).setTitle(value);
        notifyItemChanged(position);
    }

    public void refreshData() {
        notifyDataSetChanged();
    }

    public static class ColumnViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private ImageButton imageButtonDelete;
        private OnBoardColumnClick onBoardColumnClick;
        public ColumnViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
            itemView.setOnClickListener(view -> {
                if (onBoardColumnClick != null){
                    onBoardColumnClick.onClick(view, getAdapterPosition());
                }
            });
        }
        private void initView(View itemView) {
            textViewName = itemView.findViewById(R.id.textViewName);
            imageButtonDelete = itemView.findViewById(R.id.imageButtonDelete);
            imageButtonDelete.setOnClickListener(view-> onBoardColumnClick.onDelete(view, getAdapterPosition()));
        }
    }
}

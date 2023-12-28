package com.unipi.msc.raiseupandroid.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.msc.raiseupandroid.Interface.OnBoardColumnClick;
import com.unipi.msc.raiseupandroid.Model.Column;
import com.unipi.msc.raiseupandroid.R;

import java.util.List;

public class BoardCreationColumnAdapter extends RecyclerView.Adapter<BoardCreationColumnAdapter.ColumnViewHolder> {
    Activity a;
    List<String> columns;
    OnBoardColumnClick onBoardColumnClick;

    public BoardCreationColumnAdapter(Activity a, List<String> columns, OnBoardColumnClick onBoardColumnClick) {
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
        holder.onBoardColumnClick = onBoardColumnClick;
        holder.textViewName.setText(columns.get(position));
    }

    @Override
    public int getItemCount() {
        return columns.size();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void deleteItem(String s) {
        columns.remove(s);
        notifyDataSetChanged();
    }

    public void addData(String column) {
        columns.add(columns.size() - 1, column);
        notifyDataSetChanged();
    }

    public void editValue(int position, String value) {
        columns.set(position,value);
        notifyDataSetChanged();
    }

    public void setData(List<Column> columns) {
        this.columns.clear();
        columns.forEach(column -> this.columns.add(column.getTitle()));
        notifyDataSetChanged();
    }

    public static class ColumnViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private ImageButton imageButtonDelete;
        private OnBoardColumnClick onBoardColumnClick;
        public ColumnViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
            itemView.setOnClickListener(view -> onBoardColumnClick.onClick(view,getAdapterPosition()));
        }
        private void initView(View itemView) {
            textViewName = itemView.findViewById(R.id.textViewName);
            imageButtonDelete = itemView.findViewById(R.id.imageButtonDelete);
            imageButtonDelete.setOnClickListener(view-> onBoardColumnClick.onDelete(view, getAdapterPosition()));
        }
    }
}

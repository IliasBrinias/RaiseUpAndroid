package com.unipi.msc.raiseupandroid.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.msc.raiseupandroid.Interface.OnColumnChangeClick;
import com.unipi.msc.raiseupandroid.Model.Column;
import com.unipi.msc.raiseupandroid.R;

import java.util.List;
import java.util.Objects;

public class ChangeColumnAdapter extends RecyclerView.Adapter<ChangeColumnAdapter.ColumnViewHolder> {
    Activity a;
    List<Column> columnList;
    OnColumnChangeClick onColumnChangeClick;
    Column currentColumn;
    public ChangeColumnAdapter(Activity a, Column currentColumn, List<Column> columnList, OnColumnChangeClick onColumnChangeClick) {
        this.a = a;
        this.currentColumn = currentColumn;
        this.columnList = columnList;
        this.onColumnChangeClick = onColumnChangeClick;
    }

    @NonNull
    @Override
    public ChangeColumnAdapter.ColumnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.column_line_layout, parent, false);
        return new ColumnViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ColumnViewHolder holder, int position) {
        holder.onColumnChangeClick = onColumnChangeClick;
        holder.bindData(Objects.equals(columnList.get(position).getId(), currentColumn.getId()),columnList.get(position));
    }
    @Override
    public int getItemCount() {
        return columnList.size();
    }

    public void refreshData() {
        notifyDataSetChanged();
    }

    public static class ColumnViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout linearLayout;
        OnColumnChangeClick onColumnChangeClick;
        public ColumnViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
        }
        private void initViews(View view) {
            textView = view.findViewById(R.id.textViewColumnName);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
        public void bindData(boolean isSelected,Column column){
            textView.setText(column.getTitle());
            linearLayout.setSelected(isSelected);
            linearLayout.setOnClickListener(view-> onColumnChangeClick.onClick(view, getAdapterPosition()));
        }
    }
}

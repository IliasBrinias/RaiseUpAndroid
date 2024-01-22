package com.unipi.msc.riseupandroid.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.msc.riseupandroid.Interface.OnNavTagClickListener;
import com.unipi.msc.riseupandroid.Model.Tag;
import com.unipi.msc.riseupandroid.R;

import java.util.List;

public class NavTagAdapter extends RecyclerView.Adapter<NavTagAdapter.NatTagViewHolder> {
    Context c;
    List<Tag> tagList;
    OnNavTagClickListener onTagClick;

    public NavTagAdapter(Context c, List<Tag> tagList, OnNavTagClickListener onTagClick) {
        this.c = c;
        this.tagList = tagList;
        this.onTagClick = onTagClick;
    }

    @NonNull
    @Override
    public NatTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_tag_layout, parent, false);
        return new NatTagViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NatTagViewHolder holder, int position) {
        holder.setOnTaskClick(onTagClick);
        holder.bindData(c, tagList.get(position));
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public void setData(List<Tag> tagList) {
        this.tagList = tagList;
        notifyDataSetChanged();
    }

    public void removeElement(Tag tag) {
        tagList.remove(tag);
        notifyDataSetChanged();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public static class NatTagViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewTaskColor;
        TextView textViewTagName;
        ImageButton imageButtonDeleteTask, imageButtonEditTask;
        private OnNavTagClickListener onTaskClick;
        public NatTagViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
            initListeners();
        }

        private void initListeners() {
            imageButtonEditTask.setOnClickListener(view -> onTaskClick.onEdit(view,getAdapterPosition()));
            imageButtonDeleteTask.setOnClickListener(view -> onTaskClick.onDelete(view,getAdapterPosition()));
        }

        private void initViews(View view) {
            imageViewTaskColor = view.findViewById(R.id.imageViewTaskColor);
            textViewTagName = view.findViewById(R.id.textViewTagName);
            imageButtonEditTask = view.findViewById(R.id.imageButtonEditTask);
            imageButtonDeleteTask = view.findViewById(R.id.imageButtonDeleteTask);
        }

        public void bindData(Context c, Tag task){
            imageViewTaskColor.setColorFilter(Color.parseColor(task.getColor()), PorterDuff.Mode.ADD);
            textViewTagName.setText(task.getName());
        }

        public void setOnTaskClick(OnNavTagClickListener onTaskClick) {
            this.onTaskClick = onTaskClick;
        }
    }
}

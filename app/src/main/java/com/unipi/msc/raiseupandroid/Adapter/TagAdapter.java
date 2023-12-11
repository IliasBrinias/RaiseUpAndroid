package com.unipi.msc.raiseupandroid.Adapter;

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

import com.unipi.msc.raiseupandroid.Interface.OnNavTagClickListener;
import com.unipi.msc.raiseupandroid.Model.Tag;
import com.unipi.msc.raiseupandroid.R;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {
    Context c;
    List<Tag> tagList;

    public TagAdapter(Context c, List<Tag> tagList) {
        this.c = c;
        this.tagList = tagList;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_tag_layout, parent, false);
        return new TagViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        holder.bindData(c, tagList.get(position));
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewTaskColor;
        TextView textViewTagName;
        ImageButton imageButtonDeleteTask, imageButtonEditTask;
        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
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
    }
}

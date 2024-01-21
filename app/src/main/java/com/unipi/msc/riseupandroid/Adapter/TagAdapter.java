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

import com.unipi.msc.riseupandroid.Interface.OnTagClick;
import com.unipi.msc.riseupandroid.Model.Tag;
import com.unipi.msc.riseupandroid.R;

import java.util.List;
import java.util.Objects;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {
    private final Context c;
    private List<Tag> tagList;
    private List<Tag> allReadySelectedTags;
    private final int layout;
    private OnTagClick onTagClick;
    private boolean isDeletable = false;

    public TagAdapter(Context c, List<Tag> tagList) {
        this.c = c;
        this.tagList = tagList;
        this.layout = R.layout.task_tag_layout;
    }
    public TagAdapter(Context c, List<Tag> tagList, OnTagClick onTagClick) {
        this.c = c;
        this.isDeletable = true;
        this.tagList = tagList;
        this.layout = R.layout.task_tag_layout;
        this.onTagClick = onTagClick;
    }

    public TagAdapter(Context c, List<Tag> tagList, List<Tag> allReadySelectedTags, int layout, OnTagClick onTagClick) {
        this.c = c;
        this.tagList = tagList;
        this.allReadySelectedTags = allReadySelectedTags;
        this.layout = layout;
        this.onTagClick = onTagClick;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new TagViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        holder.onTagClick = onTagClick;
        if (allReadySelectedTags == null){
            holder.bindData(c, isDeletable, false, tagList.get(position));
        }else {
            holder.bindData(c, isDeletable, allReadySelectedTags.stream().anyMatch(selectedTag -> Objects.equals(selectedTag.getId(), tagList.get(position).getId())), tagList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public void setData(List<Tag> tags) {
        tagList = tags;
        notifyDataSetChanged();
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewTaskColor;
        private TextView textViewTagName;
        private ImageButton imageButtonDelete;
        private OnTagClick onTagClick;
        private final View itemView;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            initViews();
            initListeners();
        }

        private void initListeners() {
            itemView.setOnClickListener(v->{
                if (onTagClick==null) return;
                v.setSelected(!v.isSelected());
                onTagClick.onClick(v,getAdapterPosition());
            });
            if (imageButtonDelete!=null){
                imageButtonDelete.setOnClickListener(view -> {
                    if (onTagClick==null) return;
                    onTagClick.onDelete(view, getAdapterPosition());
                });
            }
        }

        private void initViews() {
            imageViewTaskColor = itemView.findViewById(R.id.imageViewTaskColor);
            textViewTagName = itemView.findViewById(R.id.textViewTagName);
            imageButtonDelete = itemView.findViewById(R.id.imageButtonDelete);
        }
        public void bindData(Context c, boolean isDeletable, boolean isSelected, Tag task){

            if (isSelected) itemView.performClick();
            imageViewTaskColor.setColorFilter(Color.parseColor(task.getColor()), PorterDuff.Mode.ADD);
            textViewTagName.setText(task.getName());
            if (imageButtonDelete != null) imageButtonDelete.setVisibility(isDeletable?View.VISIBLE:View.GONE);
        }
    }
}

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

import com.unipi.msc.raiseupandroid.Interface.OnTagClick;
import com.unipi.msc.raiseupandroid.Model.Tag;
import com.unipi.msc.raiseupandroid.R;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {
    private final Context c;
    private List<Tag> tagList;
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

    public TagAdapter(Context c, List<Tag> tagList, int layout, OnTagClick onTagClick) {
        this.c = c;
        this.tagList = tagList;
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
        holder.bindData(c, isDeletable, tagList.get(position));
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

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
            initListeners(itemView);
        }

        private void initListeners(View itemView) {
            itemView.setOnClickListener(v->{
                if (onTagClick==null) return;
                itemView.setSelected(!itemView.isSelected());
                onTagClick.onClick(v,getAdapterPosition());
            });
            if (imageButtonDelete!=null){
                imageButtonDelete.setOnClickListener(view -> {
                    if (onTagClick==null) return;
                    onTagClick.onDelete(view, getAdapterPosition());
                });
            }
        }

        private void initViews(View view) {
            imageViewTaskColor = view.findViewById(R.id.imageViewTaskColor);
            textViewTagName = view.findViewById(R.id.textViewTagName);
            imageButtonDelete = view.findViewById(R.id.imageButtonDelete);
        }
        public void bindData(Context c, boolean isDeletable, Tag task){
            imageViewTaskColor.setColorFilter(Color.parseColor(task.getColor()), PorterDuff.Mode.ADD);
            textViewTagName.setText(task.getName());
            if (imageButtonDelete != null) imageButtonDelete.setVisibility(isDeletable?View.VISIBLE:View.GONE);
        }
    }
}

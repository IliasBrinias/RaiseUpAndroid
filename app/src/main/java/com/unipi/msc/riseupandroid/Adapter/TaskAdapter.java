package com.unipi.msc.riseupandroid.Adapter;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.msc.riseupandroid.Activity.MainActivity;
import com.unipi.msc.riseupandroid.Interface.OnTaskClick;
import com.unipi.msc.riseupandroid.Model.Task;
import com.unipi.msc.riseupandroid.R;
import com.unipi.msc.riseupandroid.Tools.ActivityUtils;
import com.unipi.msc.riseupandroid.Tools.ImageUtils;
import com.unipi.msc.riseupandroid.Tools.RecyclerViewUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    Activity a;
    List<Task> taskList;
    OnTaskClick onTaskClick;
    boolean fistColumn, lastColumn;
    public TaskAdapter(Activity a, List<Task> taskList, OnTaskClick onTaskClick) {
        this.a = a;
        this.taskList = taskList;
        this.onTaskClick = onTaskClick;
    }
    public TaskAdapter(Activity a, List<Task> taskList, boolean fistColumn, boolean lastColumn, OnTaskClick onTaskClick) {
        this.a = a;
        this.taskList = taskList;
        this.fistColumn = fistColumn;
        this.lastColumn = lastColumn;
        this.onTaskClick = onTaskClick;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.setOnTaskClick(onTaskClick);
        holder.bindData(a, fistColumn, lastColumn, taskList.get(position));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void refreshData() {
        notifyDataSetChanged();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView0, imageView1, imageView2;
        private ImageButton imageButtonDeleteTask, imageButtonNextColumn, imageButtonPreviousColumn;
        private ImageView imageViewStatus;
        private TextView textViewTitle, textViewDueDate, textViewDescription;
        private CardView cardViewProfile0,cardViewProfile1,cardViewProfile2;
        private RecyclerView recyclerView;
        private LinearLayoutCompat linearLayout;
        private View expireLayout;
        private OnTaskClick onTaskClick;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
            initListeners(itemView);
        }
        private void initViews(View view) {
            imageView0 = view.findViewById(R.id.imageViewProfile0);
            imageView1 = view.findViewById(R.id.imageViewProfile1);
            imageView2 = view.findViewById(R.id.imageViewProfile2);
            textViewTitle = view.findViewById(R.id.textViewTaskTitle);
            textViewDescription = view.findViewById(R.id.textViewDescription);
            textViewDueDate = view.findViewById(R.id.textViewDueDate);
            imageButtonDeleteTask = view.findViewById(R.id.imageButtonDeleteTask);
            imageButtonNextColumn = view.findViewById(R.id.imageButtonNextColumn);
            imageButtonPreviousColumn = view.findViewById(R.id.imageButtonPreviousColumn);
            imageViewStatus = view.findViewById(R.id.imageViewStatus);
            linearLayout = view.findViewById(R.id.linearLayout);
            expireLayout = view.findViewById(R.id.expireLayout);
            cardViewProfile0 = view.findViewById(R.id.cardViewProfile0);
            cardViewProfile1 = view.findViewById(R.id.cardViewProfile1);
            cardViewProfile2 = view.findViewById(R.id.cardViewProfile2);
            recyclerView = view.findViewById(R.id.recyclerView);
        }
        private void initListeners(View itemView) {
            itemView.setOnClickListener(view-> onTaskClick.onClick(view,getAdapterPosition()));
            imageButtonDeleteTask.setOnClickListener(v->onTaskClick.onDelete(v,getAdapterPosition()));
            imageButtonNextColumn.setOnClickListener(v->onTaskClick.onNextColumn(v,getAdapterPosition()));
            imageButtonPreviousColumn.setOnClickListener(v->onTaskClick.onPreviousColumn(v,getAdapterPosition()));
        }
        public void bindData(Activity a, boolean fistColumn, boolean lastColumn, Task task){
            imageViewStatus.setVisibility(View.GONE);
            if (a instanceof MainActivity){
                imageButtonDeleteTask.setVisibility(View.GONE);
                imageButtonNextColumn.setVisibility(View.GONE);
                imageButtonPreviousColumn.setVisibility(View.GONE);
            }else{
                linearLayout.setBackgroundTintList(ColorStateList.valueOf(ActivityUtils.getColor(a,R.attr.back_color)));
                if (fistColumn){
                    imageButtonPreviousColumn.setVisibility(View.GONE);
                }else {
                    imageButtonPreviousColumn.setVisibility(View.VISIBLE);
                }
                if (lastColumn){
                    imageButtonNextColumn.setVisibility(View.GONE);
                }else{
                    imageButtonNextColumn.setVisibility(View.VISIBLE);
                }
                if (task.getCompleted()){
                    imageViewStatus.setVisibility(View.VISIBLE);
                    imageViewStatus.setImageResource(R.drawable.ic_bookmark_complete);
                }
            }
            if (task.getTags() == null){
                recyclerView.setVisibility(View.GONE);
            }else{
                recyclerView.setLayoutManager(RecyclerViewUtils.getFlexLayout(a));
                recyclerView.setAdapter(new TagAdapter(a,task.getTags()));
                recyclerView.setVisibility(View.VISIBLE);
            }
            textViewTitle.setText(task.getTitle());
            if (task.getDescription() == null){
                textViewDescription.setVisibility(View.GONE);
            }else{
                textViewDescription.setText(task.getDescription());
                textViewDescription.setVisibility(View.VISIBLE);
            }
            if (task.getDueDate() == null){
                textViewDueDate.setVisibility(View.GONE);
                expireLayout.setVisibility(View.GONE);
            }else {
                textViewDueDate.setText(ActivityUtils.normalizeDate(task.getDueDate()));
                long days = ActivityUtils.getDifferenceDays(new Date().getTime(),task.getDueDate());
                if (days<=0 && !task.getCompleted()){
                    imageViewStatus.setVisibility(View.VISIBLE);
                    imageViewStatus.setImageResource(R.drawable.ic_bookmark_expired);
                }
            }
            try{
                ImageUtils.loadProfileToImageView(a,task.getUsers().get(0).getProfile(),imageView0);
                cardViewProfile0.setVisibility(View.VISIBLE);
            }catch (Exception ignore){
                cardViewProfile0.setVisibility(View.GONE);
            }
            try {
                ImageUtils.loadProfileToImageView(a,task.getUsers().get(1).getProfile(),imageView1);
                cardViewProfile1.setVisibility(View.VISIBLE);
            }catch (Exception ignore){
                cardViewProfile1.setVisibility(View.GONE);
            }
            try {
                ImageUtils.loadProfileToImageView(a,task.getUsers().get(2).getProfile(),imageView2);
                cardViewProfile2.setVisibility(View.VISIBLE);
            }catch (Exception ignore){
                cardViewProfile2.setVisibility(View.GONE);
            }
        }
        public void setOnTaskClick(OnTaskClick onTaskClick) {
            this.onTaskClick = onTaskClick;
        }
    }
}

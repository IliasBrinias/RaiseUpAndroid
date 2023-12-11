package com.unipi.msc.raiseupandroid.Adapter;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.msc.raiseupandroid.Activity.MainActivity;
import com.unipi.msc.raiseupandroid.Fragment.TaskFragment;
import com.unipi.msc.raiseupandroid.Interface.OnTaskClick;
import com.unipi.msc.raiseupandroid.Model.Task;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Tools.ActivityUtils;
import com.unipi.msc.raiseupandroid.Tools.ImageUtils;

import java.text.SimpleDateFormat;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    Activity a;
    List<Task> taskList;
    OnTaskClick onTaskClick;

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
    public TaskAdapter(Activity a, List<Task> taskList, OnTaskClick onTaskClick) {
        this.a = a;
        this.taskList = taskList;
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
        holder.bindData(a, taskList.get(position));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView0, imageView1, imageView2;
        private ImageButton imageButtonDeleteTask, imageButtonChangeColumn;
        private TextView textViewTitle, textViewDueDate, textViewDescription;
        private OnTaskClick onTaskClick;
        private LinearLayoutCompat linearLayout;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
            initListeners();
        }

        private void initListeners() {
            itemView.setOnClickListener(view-> onTaskClick.onClick(view,getAdapterPosition()));
            imageButtonDeleteTask.setOnClickListener(v->onTaskClick.onDelete(v,getAdapterPosition()));
            imageButtonChangeColumn.setOnClickListener(v->onTaskClick.onChangeColumn(v,getAdapterPosition()));
        }

        private void initViews(View view) {
            imageView0 = view.findViewById(R.id.imageViewProfile0);
            imageView1 = view.findViewById(R.id.imageViewProfile1);
            imageView2 = view.findViewById(R.id.imageViewProfile2);
            textViewTitle = view.findViewById(R.id.textViewTaskTitle);
            textViewDescription = view.findViewById(R.id.textViewDescription);
            textViewDueDate = view.findViewById(R.id.textViewDueDate);
            imageButtonDeleteTask = view.findViewById(R.id.imageButtonDeleteTask);
            imageButtonChangeColumn = view.findViewById(R.id.imageButtonChangeColumn);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
        public void bindData(Activity a, Task task){
            if (a instanceof MainActivity){
                imageButtonDeleteTask.setVisibility(View.GONE);
                imageButtonChangeColumn.setVisibility(View.GONE);
            }else{
                linearLayout.setBackgroundTintList(ColorStateList.valueOf(ActivityUtils.getColor(a,R.attr.back_color)));
            }
            textViewTitle.setText(task.getName());
            textViewDescription.setText(task.getDsc());
            textViewDueDate.setText(simpleDateFormat.format(task.getDueDate()));
            try{
                ImageUtils.loadProfileToImageView(a,task.getEmployees().get(0).getProfileURL(),imageView0);
                ImageUtils.loadProfileToImageView(a,task.getEmployees().get(1).getProfileURL(),imageView1);
                ImageUtils.loadProfileToImageView(a,task.getEmployees().get(2).getProfileURL(),imageView2);
            }catch (Exception ignore){}
        }
        public void setOnTaskClick(OnTaskClick onTaskClick) {
            this.onTaskClick = onTaskClick;
        }
    }
}

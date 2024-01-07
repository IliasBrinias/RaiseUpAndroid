package com.unipi.msc.riseupandroid.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.msc.riseupandroid.Model.User;
import com.unipi.msc.riseupandroid.Model.UserStatistic;
import com.unipi.msc.riseupandroid.R;
import com.unipi.msc.riseupandroid.Tools.ImageUtils;

import java.util.List;

public class EmployeeStatisticsAdapter extends RecyclerView.Adapter<EmployeeStatisticsAdapter.EmployeeStatisticsHolder> {
    Activity a;
    List<UserStatistic> userList;

    public EmployeeStatisticsAdapter(Activity a, List<UserStatistic> userList) {
        this.a = a;
        this.userList = userList;
    }

    @NonNull
    @Override
    public EmployeeStatisticsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_employee_layout, parent, false);
        return new EmployeeStatisticsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeStatisticsHolder holder, int position) {
        holder.bindData(a, userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class EmployeeStatisticsHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewEmployee, imageViewWinner;
        private TextView textViewEmployeeName, textViewCompletedTasks;
        public EmployeeStatisticsHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View view) {
            imageViewEmployee = view.findViewById(R.id.imageViewEmployeeImage);
            imageViewWinner = view.findViewById(R.id.imageViewWinner);
            textViewEmployeeName = view.findViewById(R.id.textViewEmployeeName);
            textViewCompletedTasks = view.findViewById(R.id.textViewCompletedTasks);
        }

        public void bindData(Activity a, UserStatistic userStatistic) {
            textViewEmployeeName.setText(userStatistic.getUser().getFullName());
            textViewCompletedTasks.setText(String.valueOf(userStatistic.getCompletedTask()));
            imageViewWinner.setVisibility(getAdapterPosition() == 0?View.VISIBLE:View.GONE);
            try {
                ImageUtils.loadProfileToImageView(a, userStatistic.getUser().getProfile(),imageViewEmployee);
            }catch (Exception ignore){}
        }
    }
}

package com.unipi.msc.raiseupandroid.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.msc.raiseupandroid.Model.User;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Tools.ImageUtils;

import java.util.List;

public class EmployeeStatisticsAdapter extends RecyclerView.Adapter<EmployeeStatisticsAdapter.EmployeeStatisticsHolder> {
    Activity a;
    List<User> userList;

    public EmployeeStatisticsAdapter(Activity a, List<User> userList) {
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
        private ImageView imageViewEmployee, imageViewUp, imageViewDown;
        private TextView textViewEmployeeName, textViewPercentage;
        public EmployeeStatisticsHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View view) {
            imageViewEmployee = view.findViewById(R.id.imageViewEmployeeImage);
            textViewEmployeeName = view.findViewById(R.id.textViewEmployeeName);
            textViewPercentage = view.findViewById(R.id.textViewPercentage);
            imageViewUp = view.findViewById(R.id.imageViewUp);
            imageViewDown = view.findViewById(R.id.imageViewDown);
        }

        public void bindData(Activity a, User user) {
            textViewEmployeeName.setText(user.getFullName());
            textViewPercentage.setText(String.valueOf(Math.abs(user.getPercentage())));
            try {
                ImageUtils.loadProfileToImageView(a, user.getProfileURL(),imageViewEmployee);
            }catch (Exception ignore){}
            if (user.getPercentage()>=0){
                imageViewDown.setVisibility(View.GONE);
            }else{
                imageViewUp.setVisibility(View.GONE);
            }
        }
    }
}

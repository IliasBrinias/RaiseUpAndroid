package com.unipi.msc.riseupandroid.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.msc.riseupandroid.Interface.OnAddEmployeeClickListener;
import com.unipi.msc.riseupandroid.Model.User;
import com.unipi.msc.riseupandroid.R;
import com.unipi.msc.riseupandroid.Tools.ImageUtils;

import java.util.List;
import java.util.Objects;

public class AddEmployeeAdapter extends RecyclerView.Adapter<AddEmployeeAdapter.AddEmployeeViewHolder> {
    Activity a;
    List<User> users;
    List<User> alreadySelectedUsers;
    OnAddEmployeeClickListener onClickListener;
    public AddEmployeeAdapter(Activity a, List<User> alreadySelectedUsers,List<User> users, OnAddEmployeeClickListener onClickListener) {
        this.a = a;
        this.alreadySelectedUsers = alreadySelectedUsers;
        this.users = users;
        this.onClickListener = onClickListener;
    }
    @NonNull
    @Override
    public AddEmployeeAdapter.AddEmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_employee_line_layout, parent, false);
        return new AddEmployeeAdapter.AddEmployeeViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull AddEmployeeAdapter.AddEmployeeViewHolder holder, int position) {
        boolean isAllReadySelected = alreadySelectedUsers.stream().anyMatch(alreadySelectedUser-> Objects.equals(alreadySelectedUser.getId(), users.get(position).getId()));
        holder.bindData(a, isAllReadySelected, users.get(position), onClickListener);
    }
    @Override
    public int getItemCount() {
        return users.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void resetItems() {
        notifyDataSetChanged();
    }
    public static class AddEmployeeViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewProfile;
        private TextView textViewName;
        private OnAddEmployeeClickListener onAddEmployeeClickListener;
        private View itemView;
        public AddEmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            initView();
            listeners();
        }
        private void listeners() {
            itemView.setOnClickListener(view -> {
                view.setSelected(!view.isSelected());
                onAddEmployeeClickListener.onClick(view, getAdapterPosition());
            });
        }
        private void initView() {
            imageViewProfile = itemView.findViewById(R.id.imageViewProfile);
            textViewName = itemView.findViewById(R.id.textViewName);
        }
        public void bindData(Activity a, boolean isAllReadySelected, User user, OnAddEmployeeClickListener listener){
            ImageUtils.loadProfileToImageView(a, user.getProfile(), imageViewProfile);
            textViewName.setText(user.getFullName());
            onAddEmployeeClickListener = listener;
            if (isAllReadySelected) itemView.performClick();
        }
    }
}

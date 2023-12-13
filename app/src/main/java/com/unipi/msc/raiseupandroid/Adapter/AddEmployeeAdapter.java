package com.unipi.msc.raiseupandroid.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.msc.raiseupandroid.Interface.OnAddEmployeeClickListener;
import com.unipi.msc.raiseupandroid.Model.User;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Tools.ImageUtils;

import java.util.List;

public class AddEmployeeAdapter extends RecyclerView.Adapter<AddEmployeeAdapter.AddEmployeeViewHolder> {
    Activity a;
    List<User> users;
    OnAddEmployeeClickListener onClickListener;

    public AddEmployeeAdapter(Activity a, List<User> users, OnAddEmployeeClickListener onClickListener) {
        this.a = a;
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
        holder.bindData(a, users.get(position), onClickListener);
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
        public AddEmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
            itemView.setOnClickListener(view -> {
                view.setSelected(true);
                onAddEmployeeClickListener.onClick(view, getAdapterPosition());
            });
        }

        private void initView(View itemView) {
            imageViewProfile = itemView.findViewById(R.id.imageViewProfile);
            textViewName = itemView.findViewById(R.id.textViewName);
        }
        public void bindData(Activity a, User user, OnAddEmployeeClickListener listener){
            ImageUtils.loadProfileToImageView(a, user.getProfile(), imageViewProfile);
            textViewName.setText(user.getFullName());
            onAddEmployeeClickListener = listener;
        }
    }
}

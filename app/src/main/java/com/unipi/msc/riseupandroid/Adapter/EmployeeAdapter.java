package com.unipi.msc.riseupandroid.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.msc.riseupandroid.Interface.OnBoardEmployeeDelete;
import com.unipi.msc.riseupandroid.Model.User;
import com.unipi.msc.riseupandroid.R;
import com.unipi.msc.riseupandroid.Tools.ImageUtils;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    Activity a;
    List<User> users;
    OnBoardEmployeeDelete onBoardEmployeeDelete;
    public EmployeeAdapter(Activity a, List<User> users, OnBoardEmployeeDelete onBoardEmployeeDelete) {
        this.a = a;
        this.users = users;
        this.onBoardEmployeeDelete = onBoardEmployeeDelete;
    }
    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_creation_employee_layout, parent, false);
        return new EmployeeViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        holder.onBoardEmployeeDelete = onBoardEmployeeDelete;
        holder.bindData(a, users.get(position));
    }
    @Override
    public int getItemCount() {
        return users.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public void setData(List<User> employees) {
        users.clear();
        users.addAll(employees);
        notifyDataSetChanged();
    }
    public void deleteItem(User user) {
        users.remove(user);
        notifyDataSetChanged();
    }

    public void addUser(User user) {
        users.add(user);
        notifyItemInserted(users.size() - 1);
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProfile;
        TextView textViewName;
        ImageButton imageButtonDelete;
        OnBoardEmployeeDelete onBoardEmployeeDelete;
        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
        }
        private void initViews(View view) {
            imageViewProfile = view.findViewById(R.id.imageViewProfile);
            textViewName = view.findViewById(R.id.textViewName);
            imageButtonDelete = view.findViewById(R.id.imageButtonDelete);
            imageButtonDelete.setOnClickListener(v->onBoardEmployeeDelete.onDelete(v,getAdapterPosition()));
        }
        public void bindData(Activity a, User user){
            ImageUtils.loadProfileToImageView(a,user.getProfile(),imageViewProfile);
            textViewName.setText(user.getFullName());
        }
    }
}

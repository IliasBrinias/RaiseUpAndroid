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

import com.unipi.msc.riseupandroid.Interface.OnAddEmployeeClickListener;
import com.unipi.msc.riseupandroid.Model.User;
import com.unipi.msc.riseupandroid.R;
import com.unipi.msc.riseupandroid.Tools.ImageUtils;

import java.util.List;

public class RecommendedEmployeeAdapter extends RecyclerView.Adapter<RecommendedEmployeeAdapter.ViewHolder>{
    Activity a;
    List<User> users;
    OnAddEmployeeClickListener onAddEmployeeClickListener;

    public RecommendedEmployeeAdapter(Activity a, List<User> users, OnAddEmployeeClickListener onAddEmployeeClickListener) {
        this.a = a;
        this.users = users;
        this.onAddEmployeeClickListener = onAddEmployeeClickListener;
    }

    @NonNull
    @Override
    public RecommendedEmployeeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_employee_layout, parent, false);
        return new RecommendedEmployeeAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedEmployeeAdapter.ViewHolder holder, int position) {
        holder.bindData(a,users.get(position),onAddEmployeeClickListener);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void remove(int position) {
        users.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewProfile;
        TextView textViewName;
        ImageButton imageButtonAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View itemView) {
            imageViewProfile = itemView.findViewById(R.id.imageViewProfile);
            textViewName = itemView.findViewById(R.id.textViewName);
            imageButtonAdd = itemView.findViewById(R.id.imageButtonAdd);
        }

        public void bindData(Activity a, User user, OnAddEmployeeClickListener onAddEmployeeClickListener) {
            ImageUtils.loadProfileToImageView(a, user.getProfile(), imageViewProfile);
            textViewName.setText(user.getFullName());
            imageButtonAdd.setOnClickListener(v->onAddEmployeeClickListener.onClick(v,getAdapterPosition()));
        }
    }
}

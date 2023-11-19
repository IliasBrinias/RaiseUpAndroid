package com.unipi.msc.raiseupandroid.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.msc.raiseupandroid.Interface.OnAddEmployeeClickListener;
import com.unipi.msc.raiseupandroid.Model.Employee;
import com.unipi.msc.raiseupandroid.R;
import com.unipi.msc.raiseupandroid.Tools.ImageUtils;

import java.util.List;

public class AddEmployeeAdapter extends RecyclerView.Adapter<AddEmployeeAdapter.AddEmployeeViewHolder> {
    Context c;
    List<Employee> employees;
    OnAddEmployeeClickListener onClickListener;

    public AddEmployeeAdapter(Context c, List<Employee> employees, OnAddEmployeeClickListener onClickListener) {
        this.c = c;
        this.employees = employees;
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
        holder.bindData(c, employees.get(position), onClickListener);
    }

    @Override
    public int getItemCount() {
        return employees.size();
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
        public void bindData(Context c, Employee employee, OnAddEmployeeClickListener listener){
            ImageUtils.loadProfileToImageView(c, employee.getProfileURL(), imageViewProfile);
            textViewName.setText(employee.getFullName());
            onAddEmployeeClickListener = listener;
        }
    }
}

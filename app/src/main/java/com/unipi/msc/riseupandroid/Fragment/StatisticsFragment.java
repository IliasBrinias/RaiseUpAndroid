package com.unipi.msc.riseupandroid.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unipi.msc.riseupandroid.Adapter.EmployeeStatisticsAdapter;
import com.unipi.msc.riseupandroid.Enum.Role;
import com.unipi.msc.riseupandroid.Model.Progress;
import com.unipi.msc.riseupandroid.Model.User;
import com.unipi.msc.riseupandroid.Model.UserStatistic;
import com.unipi.msc.riseupandroid.R;
import com.unipi.msc.riseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.riseupandroid.Retrofit.RetrofitClient;
import com.unipi.msc.riseupandroid.Tools.ActivityUtils;
import com.unipi.msc.riseupandroid.Tools.ChartUtils;
import com.unipi.msc.riseupandroid.Tools.CustomDatePicker;
import com.unipi.msc.riseupandroid.Tools.ImageUtils;
import com.unipi.msc.riseupandroid.Tools.ItemViewModel;
import com.unipi.msc.riseupandroid.Tools.RetrofitUtils;
import com.unipi.msc.riseupandroid.Tools.UserUtils;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticsFragment extends Fragment {

    private TextView textViewStartDate, textViewEndDate;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LineChart chart;
    private EmployeeStatisticsAdapter employeeStatisticsAdapter;
    private RaiseUpAPI raiseUpAPI;
    private Long startUnixDate, endUnixDate;
    private List<UserStatistic> userStatistics = new ArrayList<>();
    private Toast t;
    private View includeUserStatistic;
    private ImageView imageViewProfile;
    private TextView textViewEmployeeName, textViewBoards, textViewCompletedTasks;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_statistics, container, false);
        initViews(v);
        initObjects();
        initListeners();
        loadData();
        return v;
    }
    private void initViews(View v) {
        textViewStartDate = v.findViewById(R.id.textViewStartDate);
        textViewEndDate = v.findViewById(R.id.textViewEndDate);
        progressBar = v.findViewById(R.id.progressBar);
        recyclerView = v.findViewById(R.id.recyclerView);
        chart = v.findViewById(R.id.lineChart);
        ChartUtils.customizeChart(chart);
        includeUserStatistic = v.findViewById(R.id.includeUserStatistic);
        imageViewProfile = includeUserStatistic.findViewById(R.id.imageViewProfile);
        textViewEmployeeName = includeUserStatistic.findViewById(R.id.textViewEmployeeName);
        textViewBoards = includeUserStatistic.findViewById(R.id.textViewBoards);
        textViewCompletedTasks = includeUserStatistic.findViewById(R.id.textViewCompletedTasks);
    }
    private void initObjects() {
        raiseUpAPI = RetrofitClient.getInstance(requireActivity()).create(RaiseUpAPI.class);
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault()).with(LocalTime.of(2, 0));
        endUnixDate = now.toInstant().toEpochMilli();
        endUnixDate = ActivityUtils.getEndOfDay(endUnixDate);
        now = now.plusDays(-7);
        startUnixDate = now.toInstant().toEpochMilli();
        textViewStartDate.setText(ActivityUtils.normalizeDate(startUnixDate));
        textViewEndDate.setText(ActivityUtils.normalizeDate(endUnixDate));
        employeeStatisticsAdapter = new EmployeeStatisticsAdapter(requireActivity(), userStatistics);
    }
    private void initListeners() {
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        recyclerView.setAdapter(employeeStatisticsAdapter);
        textViewStartDate.setOnClickListener(view-> CustomDatePicker.showPicker(requireActivity(),startUnixDate, date -> {
            startUnixDate = date;
            textViewStartDate.setText(ActivityUtils.normalizeDate(date));
            loadChartData();
        }));
        textViewEndDate.setOnClickListener(view-> CustomDatePicker.showPicker(requireActivity(),endUnixDate, date -> {
            endUnixDate = date;
            textViewEndDate.setText(ActivityUtils.normalizeDate(date));
            loadChartData();
        }));
    }
    private User user;
    private void loadData(){
        loadChartData();
        new ViewModelProvider(requireActivity()).get(ItemViewModel.class).getUser().observe(getViewLifecycleOwner(), user -> {
            StatisticsFragment.this.user = user;
            if (user.getRole() == Role.ADMIN){
                recyclerView.setVisibility(View.VISIBLE);
                includeUserStatistic.setVisibility(View.GONE);
            }else {
                recyclerView.setVisibility(View.GONE);
                includeUserStatistic.setVisibility(View.VISIBLE);
            }
            loadUserStatistics();
        });
    }
    private void loadChartData() {
        ActivityUtils.showProgressBar(progressBar);
        raiseUpAPI.getProgress(UserUtils.loadBearerToken(requireActivity()), startUnixDate, endUnixDate).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(requireActivity(), response);
                    ActivityUtils.showToast(requireActivity(), t, msg);
                }else{
                    List<Progress> progressList = new ArrayList<>();
                    JsonArray jsonArray = response.body().get("data").getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        progressList.add(Progress.buildFromJSON(jsonArray.get(i).getAsJsonObject()));
                    }
                    showChart(progressList);
                }
                ActivityUtils.hideProgressBar(progressBar);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(requireActivity(),t);
                ActivityUtils.hideProgressBar(progressBar);
            }
        });
    }
    private void loadUserStatistics() {
        ActivityUtils.showProgressBar(progressBar);
        raiseUpAPI.getUserStatistics(UserUtils.loadBearerToken(requireActivity()), startUnixDate, endUnixDate).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    String msg = RetrofitUtils.handleErrorResponse(requireActivity(), response);
                    ActivityUtils.showToast(requireActivity(), t, msg);
                }else{
                    JsonArray jsonArray = response.body().get("data").getAsJsonArray();
                    if (user.getRole() == Role.ADMIN){
                        userStatistics.clear();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            userStatistics.add(UserStatistic.buildFromJSON(jsonArray.get(i).getAsJsonObject()));
                        }
                        employeeStatisticsAdapter.notifyDataSetChanged();
                    }else {
                        loadUsersData(UserStatistic.buildFromJSON(jsonArray.get(0).getAsJsonObject()));
                    }
                }
                ActivityUtils.hideProgressBar(progressBar);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                RetrofitUtils.handleException(requireActivity(),t);
                ActivityUtils.hideProgressBar(progressBar);
            }
        });
    }
    private void loadUsersData(UserStatistic userStatistic) {
        ImageUtils.loadProfileToImageView(requireActivity(),userStatistic.getUser().getProfile(),imageViewProfile);
        textViewEmployeeName.setText(userStatistic.getUser().getFullName());
        textViewBoards.setText(String.valueOf(userStatistic.getBoards()));
        textViewCompletedTasks.setText(String.valueOf(userStatistic.getCompletedTask()));
    }
    private void showChart(List<Progress> progressList) {
        chart.setData(ChartUtils.getDataConfig(requireActivity(),progressList));
        chart.notifyDataSetChanged();
        chart.invalidate();
    }
}
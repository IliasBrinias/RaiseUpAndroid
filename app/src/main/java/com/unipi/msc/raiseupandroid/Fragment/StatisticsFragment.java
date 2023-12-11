package com.unipi.msc.raiseupandroid.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.unipi.msc.raiseupandroid.Adapter.EmployeeStatisticsAdapter;
import com.unipi.msc.raiseupandroid.Model.User;
import com.unipi.msc.raiseupandroid.R;
import com.github.mikephil.charting.data.Entry;
import com.unipi.msc.raiseupandroid.Tools.ActivityUtils;
import com.unipi.msc.raiseupandroid.Tools.MockData;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private LineChart chart;
    private RecyclerView recyclerView;
    EmployeeStatisticsAdapter adapter;
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
        showChart();
        showEmployee();
        return v;
    }

    private void showEmployee() {
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        List<User> userList = MockData.getTestEmployees();
        adapter = new EmployeeStatisticsAdapter(requireActivity(), userList);
        recyclerView.setAdapter(adapter);
    }

    private void showChart() {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < 7; i++) {

            float val = (float) (Math.random() * 10) - 30;
            values.add(new Entry(i, val));
        }

        LineDataSet set1;
        // create a dataset and give it a type
        set1 = new LineDataSet(values,"");
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setDrawVerticalHighlightIndicator(false);
        set1.setDrawHorizontalHighlightIndicator(false);
        // black lines and points

        set1.setColor(ActivityUtils.getColor(requireActivity(), R.attr.text_color));
        set1.setCircleColor(ActivityUtils.getColor(requireActivity(), R.attr.text_color));

        // line thickness and point size
        set1.setLineWidth(2f);
        set1.setCircleRadius(3f);

        // draw points as solid circles
        set1.setDrawCircleHole(false);
        set1.setDrawValues(false);
        // text size of values
//        set1.setValueTextSize(13f);
//        set1.setValueTypeface(ResourcesCompat.getFont(requireActivity(),R.font.euclid_circular_a_regular));

        set1.setFillColor(ActivityUtils.getColor(requireActivity(), R.attr.text_color));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // set data
        chart.getLegend().setEnabled(false);
        chart.setData(data);
    }

    private void initViews(View v) {
        chart = v.findViewById(R.id.lineChart);
        // disable description text
        chart.getDescription().setEnabled(false);
        // enable touch gestures
        chart.setTouchEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        // no description text
        chart.getDescription().setEnabled(false);
        // enable touch gestures
        chart.setTouchEnabled(true);
        chart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);
        chart.setTouchEnabled(false);
        chart.setViewPortOffsets(0f, 0f, 0f, 0f);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setEnabled(false);

        recyclerView = v.findViewById(R.id.recyclerView);
    }
}
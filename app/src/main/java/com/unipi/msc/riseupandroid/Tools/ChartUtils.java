package com.unipi.msc.riseupandroid.Tools;

import android.app.Activity;

import androidx.core.content.res.ResourcesCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.unipi.msc.riseupandroid.Model.Progress;
import com.unipi.msc.riseupandroid.R;

import java.util.ArrayList;
import java.util.List;

public class ChartUtils {
    public static LineData getDataConfig(Activity activity, List<Progress> progressList){
        ArrayList<Entry> values = new ArrayList<>();
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        for (int i = 0; i < progressList.size(); i++) {
            values.add(new Entry(i, progressList.get(i).getCompletedTasks()));
        }

        // create a dataset and give it a type
        LineDataSet lineDataSet = new LineDataSet(values,"");
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawVerticalHighlightIndicator(false);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);

        // black lines and points
        lineDataSet.setColor(ActivityUtils.getColor(activity, R.attr.text_color));
        lineDataSet.setCircleColor(ActivityUtils.getColor(activity, R.attr.text_color));

        // line thickness and point size
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleRadius(3f);

        // draw points as solid circles
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setDrawValues(false);

        // text size of values
        lineDataSet.setValueTextSize(13f);
        lineDataSet.setValueTypeface(ResourcesCompat.getFont(activity,R.font.euclid_circular_a_regular));
        lineDataSet.setFillColor(ActivityUtils.getColor(activity, R.attr.text_color));
        dataSets.add(lineDataSet); // add the data sets

        // create a data object with the data sets
        return new LineData(dataSets);
    }

    public static void customizeChart(LineChart chart) {

        // disable description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

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
        chart.animateXY(200, 100);
        chart.getLegend().setEnabled(false);
    }
}

package com.unipi.msc.riseupandroid.Tools;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.unipi.msc.riseupandroid.Model.Difficulty;
import com.unipi.msc.riseupandroid.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.concurrent.TimeUnit;

public class ActivityUtils {
    public static int getColor(Activity activity, int attr){
        TypedValue typedValue = new TypedValue();
        ((Context) activity).getTheme().resolveAttribute(attr, typedValue, true);
        return ContextCompat.getColor(activity, typedValue.resourceId);
    }
    public static void showProgressBar(ProgressBar progressBar){
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }
    public static void hideProgressBar(ProgressBar progressBar){
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.GONE);
    }
    public static void showToast(Activity a, Toast t, String msg){
        if (t==null) t = Toast.makeText(a,msg,Toast.LENGTH_SHORT);
        t.cancel();
        if (msg == null || msg.isEmpty()) return;
        t.setText(msg);
        t.show();
    }
    public static long getDifferenceDays(Long d1, Long d2) {
        return TimeUnit.DAYS.convert(d2 - d1, TimeUnit.MILLISECONDS) + 1;
    }
    public static Calendar unixToCalendar(long unixTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unixTime);
        return calendar;
    }
    public static String normalizeDate(Long unixDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(unixDate);
    }
    public static String getDifficultyDsc(Activity a, Difficulty difficulty) {
        if (difficulty == null) return a.getString(R.string.enter_a_difficulty_level);
        switch (difficulty){
            case LOW: return a.getString(R.string.junior);
            case MEDIUM: return a.getString(R.string.mid_level);
            case INTERMEDIATE: return a.getString(R.string.intermediate);
            case HIGH: return a.getString(R.string.senior);
            default: return a.getString(R.string.enter_a_difficulty_level);
        }
    }
}

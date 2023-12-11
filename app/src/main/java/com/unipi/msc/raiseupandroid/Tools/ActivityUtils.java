package com.unipi.msc.raiseupandroid.Tools;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;

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
}

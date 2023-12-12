package com.unipi.msc.raiseupandroid.Tools;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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
    public static void showToast(Activity a, Toast t, String msg){
        if (t==null) t = Toast.makeText(a,msg,Toast.LENGTH_SHORT);
        t.cancel();
        t.setText(msg);
        t.show();
    }

}

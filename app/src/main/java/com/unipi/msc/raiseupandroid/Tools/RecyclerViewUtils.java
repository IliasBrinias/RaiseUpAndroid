package com.unipi.msc.raiseupandroid.Tools;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

public class RecyclerViewUtils {
    public static RecyclerView.LayoutManager getFlexLayout(Activity a){
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(a.getApplicationContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        return layoutManager;
    }
}

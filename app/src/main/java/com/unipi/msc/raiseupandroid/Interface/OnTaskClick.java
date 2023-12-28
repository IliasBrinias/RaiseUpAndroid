package com.unipi.msc.raiseupandroid.Interface;

import android.view.View;

public interface OnTaskClick {
    void onClick(View view, int position);
    default void onDelete(View view, int position){};
    default void onNextColumn(View view, int position){};
    default void onPreviousColumn(View view, int position){};
}

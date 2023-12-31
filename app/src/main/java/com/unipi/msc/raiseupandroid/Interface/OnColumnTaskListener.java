package com.unipi.msc.raiseupandroid.Interface;

import android.view.View;

public interface OnColumnTaskListener {
    void onClick(View view, int columnPosition, int taskPosition);
    default void onColumnTitleClick(View view, int position){};
    default void onTaskCreate(View view, int columnPosition){};
    default void onDelete(View view, int columnPosition, int taskPosition){};
    default void onTaskChangeColumn(View view, int columnPosition, int taskPosition, int targetColumnPosition){};
}

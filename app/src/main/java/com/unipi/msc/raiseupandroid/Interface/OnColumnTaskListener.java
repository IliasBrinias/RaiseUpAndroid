package com.unipi.msc.raiseupandroid.Interface;

import android.view.View;

public interface OnColumnTaskListener {
    void onClick(View view, int columnPosition, int taskPosition);
    default void onDelete(View view, int columnPosition, int taskPosition){};
    default void onNextColumn(View view, int columnPosition, int taskPosition){};
    default void onPreviousColumn(View view, int columnPosition, int taskPosition){};
}

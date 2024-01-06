package com.unipi.msc.riseupandroid.Interface;

import android.view.View;

public interface OnBoardClick {
    void onClick(View view, int position);
    default void addEmployees(View view, int position){}
    default boolean onLongClick(View view, int position){return false;};
}

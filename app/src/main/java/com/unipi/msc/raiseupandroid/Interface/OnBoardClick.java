package com.unipi.msc.raiseupandroid.Interface;

import android.view.View;

import com.unipi.msc.raiseupandroid.Model.Board;

public interface OnBoardClick {
    void onClick(View view, int position);
    default void addEmployees(View view, int position){}
}

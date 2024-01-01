package com.unipi.msc.riseupandroid.Interface;

import android.view.View;

public interface OnTagClick {
    void onClick(View view, int position);
    default void onDelete(View view, int position){};
}

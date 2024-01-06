package com.unipi.msc.riseupandroid.Interface;

import com.unipi.msc.riseupandroid.Model.Column;
import com.unipi.msc.riseupandroid.Retrofit.ColumnRequest;

import java.util.List;

public interface OnColumnOrderChange {
    void onResponse(List<Column> columns);
}

package com.unipi.msc.riseupandroid.Tools;

import android.app.Activity;
import android.app.DatePickerDialog;

import com.unipi.msc.riseupandroid.Interface.OnSetDate;

import java.util.Calendar;
import java.util.Date;

public class CustomDatePicker {
    public static void showPicker(Activity a, Long currentDate, OnSetDate onSetDate){
        Calendar calendar = ActivityUtils.unixToCalendar(currentDate != null ? currentDate : new Date().getTime() );
        new DatePickerDialog(a, (datePicker, year, month, day) -> {
            Calendar myCalendar = Calendar.getInstance();
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            onSetDate.onResponse(myCalendar.getTimeInMillis());
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}

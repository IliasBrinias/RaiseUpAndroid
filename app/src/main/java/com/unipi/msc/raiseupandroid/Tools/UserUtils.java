package com.unipi.msc.raiseupandroid.Tools;

import android.app.Activity;
import android.content.Intent;

import com.unipi.msc.raiseupandroid.Activity.StartActivity;

public class UserUtils {
    public static void logout(Activity a){
        a.startActivity(new Intent(a, StartActivity.class));
    }
}

package com.unipi.msc.riseupandroid.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.unipi.msc.riseupandroid.Activity.StartActivity;
import com.unipi.msc.riseupandroid.Model.User;

public class UserUtils {
    public static void logout(Activity a){
        a.getSharedPreferences(a.getPackageName() + "_preferences", Context.MODE_PRIVATE).edit().clear().apply();
        a.startActivity(new Intent(a, StartActivity.class));
    }
    public static void saveUser(Activity a, User user){
        SharedPreferences.Editor prefsEditor = a.getSharedPreferences(a.getPackageName() + "_preferences", Context.MODE_PRIVATE).edit();
        prefsEditor.putString(NameTag.USER, new Gson().toJson(user));
        prefsEditor.apply();
    }

    public static boolean userExists(Activity a) {
        return a.getSharedPreferences(a.getPackageName() + "_preferences", Context.MODE_PRIVATE).contains(NameTag.USER);
    }
    public static User loadUser(Activity a){
        String json = a.getSharedPreferences(a.getPackageName() + "_preferences", Context.MODE_PRIVATE).getString(NameTag.USER, null);
        if (json == null) logout(a);
        return new Gson().fromJson(json, User.class);
    }
    public static void saveBearerToken(Activity a, String token){
        SharedPreferences.Editor prefsEditor = a.getSharedPreferences(a.getPackageName() + "_preferences", Context.MODE_PRIVATE).edit();
        prefsEditor.putString(NameTag.USER_TOKEN, token);
        prefsEditor.apply();
    }
    public static String loadBearerToken(Activity a){
        String json = a.getSharedPreferences(a.getPackageName() + "_preferences", Context.MODE_PRIVATE).getString(NameTag.USER_TOKEN, null);
        return "Bearer " + json;
    }
}

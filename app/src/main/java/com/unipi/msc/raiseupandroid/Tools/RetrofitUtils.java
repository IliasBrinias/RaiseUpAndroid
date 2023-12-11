package com.unipi.msc.raiseupandroid.Tools;

import android.app.Activity;
import android.content.Intent;

import com.google.gson.JsonObject;
import com.unipi.msc.raiseupandroid.Activity.BadConnectionActivity;
import com.unipi.msc.raiseupandroid.Activity.RegisterActivity;
import com.unipi.msc.raiseupandroid.Retrofit.RetrofitConfig.NoConnectivityException;

import java.io.IOException;

import retrofit2.Response;

public class RetrofitUtils {
    public static final String BASE_URL = "http://192.168.1.8:8080/";
    private static final int HTTP_OK = 200;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_ACCESS_DENIED = 401;
    private static final String APPLICATION_JSON = "application/json";
    private static final String ACCESS_DENIED = "ACCESS_DENIED";

    public static void handleException(Activity a, Throwable t){
        if (t instanceof NoConnectivityException) {
            a.startActivity(new Intent(a, BadConnectionActivity.class));
        }
    }

    public static String handleErrorResponse(Activity a, Response<JsonObject> response) {
        if (response.code() == HTTP_ACCESS_DENIED) return "Bad Credentials";
        try {
            JsonObject jsonObject = response.body();
            int code = jsonObject.get("code").getAsInt();
            String msg = jsonObject.get("msg").getAsString();
            if (code == HTTP_BAD_REQUEST){
                return msg;
            } else if (code == HTTP_ACCESS_DENIED) {
                UserUtils.logout(a);
            }
        }catch (Exception ignore){}
        return "";
    }
}

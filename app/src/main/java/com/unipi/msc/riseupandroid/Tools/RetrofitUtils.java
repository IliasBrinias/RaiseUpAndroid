package com.unipi.msc.riseupandroid.Tools;

import android.app.Activity;
import android.content.Intent;

import com.google.gson.JsonObject;
import com.unipi.msc.riseupandroid.Activity.BadConnectionActivity;
import com.unipi.msc.riseupandroid.R;
import com.unipi.msc.riseupandroid.Retrofit.RetrofitConfig.NoConnectivityException;

import org.json.JSONObject;

import retrofit2.Response;

public class RetrofitUtils {
    public static final String BASE_URL = "http://192.168.1.8:8080/";
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_INTERNAL_SERVER_ERROR = 500;
    private static final int HTTP_ACCESS_DENIED = 401;

    public static void handleException(Activity a, Throwable t){
        if (t instanceof NoConnectivityException) {
            a.startActivity(new Intent(a, BadConnectionActivity.class));
        }
    }

    public static String handleErrorResponse(Activity a, Response<JsonObject> response) {
        try {
            JSONObject jsonObject = new JSONObject(response.errorBody().string());
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("message");
            if (code == HTTP_ACCESS_DENIED) {
                UserUtils.logout(a);
            }else if (code == HTTP_BAD_REQUEST || code == HTTP_INTERNAL_SERVER_ERROR){
                return normalizeMessage(a,msg);
            }
        }catch (Exception ignore){}
        return "";
    }

    private static String normalizeMessage(Activity a, String msg) {
        switch (msg) {
            case ErrorMessages.USER_NOT_FOUND:
                return a.getString(R.string.user_not_found);
            case ErrorMessages.USERNAME_EXISTS:
                return a.getString(R.string.username_exists);
            case ErrorMessages.EMAIL_EXISTS:
                return a.getString(R.string.email_exists);
            case ErrorMessages.ACCESS_DENIED:
                return a.getString(R.string.access_denied);
            case ErrorMessages.SOMETHING_HAPPENED:
                return a.getString(R.string.something_happened);
            case ErrorMessages.TAG_NOT_FOUND:
                return a.getString(R.string.tag_not_found);
            case ErrorMessages.NO_COLUMNS_FOUND:
                return a.getString(R.string.no_columns_found);
            case ErrorMessages.BOARD_NOT_FOUND:
                return a.getString(R.string.board_not_found);
            case ErrorMessages.TASK_NOT_FOUND:
                return a.getString(R.string.task_not_found);
            case ErrorMessages.STEP_NOT_FOUND:
                return a.getString(R.string.step_not_found);
            case ErrorMessages.TITLE_IS_MANDATORY:
                return a.getString(R.string.title_is_mandatory);
            case ErrorMessages.COMPLETE_THE_MANDATORY_FIELDS:
                return a.getString(R.string.complete_the_mandatory_fields);
            case ErrorMessages.PLEASE_TRY_AGAIN_LATER:
                return a.getString(R.string.please_try_again_later);
            case ErrorMessages.BAD_CREDENTIALS:
                return a.getString(R.string.bad_credentials);
            default:
                return "";
        }
    }
}

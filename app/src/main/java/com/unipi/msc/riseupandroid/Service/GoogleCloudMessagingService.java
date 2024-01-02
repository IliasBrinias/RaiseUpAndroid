package com.unipi.msc.riseupandroid.Service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;
import com.unipi.msc.riseupandroid.Activity.TaskActivity;
import com.unipi.msc.riseupandroid.Retrofit.RaiseUpAPI;
import com.unipi.msc.riseupandroid.Retrofit.Request.FCMRequest;
import com.unipi.msc.riseupandroid.Retrofit.RetrofitClient;
import com.unipi.msc.riseupandroid.Tools.NameTag;
import com.unipi.msc.riseupandroid.R;
import com.unipi.msc.riseupandroid.Tools.RetrofitUtils;
import com.unipi.msc.riseupandroid.Tools.UserUtils;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleCloudMessagingService extends FirebaseMessagingService {
    private RaiseUpAPI raiseUpAPI;

    @Override
    public void onCreate() {
        super.onCreate();
        raiseUpAPI = RetrofitClient.getInstance(this).create(RaiseUpAPI.class);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String taskTitle = data.getOrDefault("taskTitle", null);
        showNotification(taskTitle);
    }

    @SuppressLint("MissingPermission")
    private void showNotification(String taskTitle) {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        notificationManager.createNotificationChannel(new NotificationChannel(NameTag.CHANNEL_ID, "Smart Alert", NotificationManager.IMPORTANCE_DEFAULT));

        Intent intent = new Intent(this, TaskActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.putExtra(NameTag.ALERT_ID, alertId);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NameTag.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_app_icon)
                .setContentTitle(getString(R.string.app_name))
                .setContentInfo(getString(R.string.you_have_an_expired_card))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.task)+" '"+taskTitle+"' "+getString(R.string.expired)))
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE))
                .setChannelId(NameTag.CHANNEL_ID);
        if (notificationManager.areNotificationsEnabled()) {
            notificationManager.notify(123, builder.build());
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        raiseUpAPI.updateFCM(UserUtils.loadBearerToken(this),new FCMRequest(token)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){

                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}

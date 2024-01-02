package com.unipi.msc.riseupandroid.Retrofit.Request;

public class FCMRequest {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public FCMRequest(String token) {
        this.token = token;
    }
}

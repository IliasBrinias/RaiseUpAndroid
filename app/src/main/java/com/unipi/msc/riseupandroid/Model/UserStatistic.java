package com.unipi.msc.riseupandroid.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class UserStatistic {
    private User user;
    private Long completedTask;
    private Long boards;

    public UserStatistic() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getCompletedTask() {
        return completedTask;
    }

    public void setCompletedTask(Long completedTask) {
        this.completedTask = completedTask;
    }

    public Long getBoards() {
        return boards;
    }

    public void setBoards(Long boards) {
        this.boards = boards;
    }

    public static UserStatistic buildFromJSON(JsonObject jsonObject){
        UserStatistic userStatistic = new UserStatistic();

        try {
            userStatistic.setUser(User.buildFromJSON(jsonObject.get("user").getAsJsonObject()));
        }catch (Exception ignore){}

        try {
            userStatistic.setCompletedTask(jsonObject.get("completedTask").getAsLong());
        }catch (Exception ignore){}

        try {
            userStatistic.setBoards(jsonObject.get("boards").getAsLong());
        }catch (Exception ignore){}

        return userStatistic;
    }
}

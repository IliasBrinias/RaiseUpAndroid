package com.unipi.msc.riseupandroid.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class UserStatistic {
    private User user;
    private Long completedTask;
    private List<UserBoardStatistics> userBoardStatistics = new ArrayList<>();

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

    public List<UserBoardStatistics> getUserBoardStatistics() {
        return userBoardStatistics;
    }

    public void setUserBoardStatistics(List<UserBoardStatistics> userBoardStatistics) {
        this.userBoardStatistics = userBoardStatistics;
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
            JsonArray jsonArray = jsonObject.get("userBoard").getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                userStatistic.getUserBoardStatistics().add(UserBoardStatistics.buildFromJSON(jsonArray.get(i).getAsJsonObject()));
            }
        }catch (Exception ignore){}

        return userStatistic;
    }
}

package com.unipi.msc.riseupandroid.Model;

import com.google.gson.JsonObject;

public class Progress {
    private Long date;
    private Long completedTasks;

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(Long completedTasks) {
        this.completedTasks = completedTasks;
    }
    public static Progress buildFromJSON(JsonObject jsonObject){
        Progress progress = new Progress();

        try {
            progress.setDate(jsonObject.get("date").getAsLong());
        }catch (Exception ignore){}

        try {
            progress.setCompletedTasks(jsonObject.get("completedTasks").getAsLong());
        }catch (Exception ignore){}

        return progress;
    }
}

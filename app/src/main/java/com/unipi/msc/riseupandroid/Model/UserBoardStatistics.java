package com.unipi.msc.riseupandroid.Model;

import com.google.gson.JsonObject;

public class UserBoardStatistics {
    private Long boardId;
    private String boardName;
    private Long openTasks;
    private Long completedTasks;

    public UserBoardStatistics() {
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public Long getOpenTasks() {
        return openTasks;
    }

    public void setOpenTasks(Long openTasks) {
        this.openTasks = openTasks;
    }

    public Long getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(Long completedTasks) {
        this.completedTasks = completedTasks;
    }
    public static UserBoardStatistics buildFromJSON(JsonObject jsonObject){
        UserBoardStatistics userBoardStatistics = new UserBoardStatistics();

        try {
            userBoardStatistics.setBoardId(jsonObject.get("boardId").getAsLong());
        }catch (Exception ignore){}

        try {
            userBoardStatistics.setBoardName(jsonObject.get("boardName").getAsString());
        }catch (Exception ignore){}

        try {
            userBoardStatistics.setOpenTasks(jsonObject.get("openTasks").getAsLong());
        }catch (Exception ignore){}

        try {
            userBoardStatistics.setCompletedTasks(jsonObject.get("completedTasks").getAsLong());
        }catch (Exception ignore){}

        return userBoardStatistics;
    }
}

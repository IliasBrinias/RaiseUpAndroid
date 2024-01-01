package com.unipi.msc.riseupandroid.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Long id;
    private String title;
    private Long date;
    private List<User> users = new ArrayList<>();
    private List<Column> columns = new ArrayList<>();
    private Long totalTasks;
    private Long ownerId;

    public Board() {
    }

    public Board(Long id, String title, Long date, List<User> users, Long totalTasks) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.users = users;
        this.totalTasks = totalTasks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Long getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(Long totalTasks) {
        this.totalTasks = totalTasks;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public static Board buildBoardFromJson(JsonObject data){
        Board board = new Board();

        try {
            board.setId(data.get("id").getAsLong());
        }catch (Exception ignore){}

        try {
            board.setTitle(data.get("title").getAsString());
        }catch (Exception ignore){}

        try {
            board.setDate(data.get("date").getAsLong());
        }catch (Exception ignore){}

        try {
            JsonArray jsonArray = data.get("steps").getAsJsonArray();
            for (int i=0; i<jsonArray.size();i++) {
                board.getColumns().add(Column.buildFromJSON(jsonArray.get(i).getAsJsonObject()));
            }
        }catch (Exception ignore){}

        try {
            JsonArray jsonArray = data.get("users").getAsJsonArray();
            for (int i=0; i<jsonArray.size();i++) {
                board.getUsers().add(User.buildFromJSON(jsonArray.get(i).getAsJsonObject()));
            }
        }catch (Exception ignore){}

        try {
            board.setTotalTasks(data.get("totalTasks").getAsLong());
        }catch (Exception ignore){}

        try {
            board.setOwnerId(data.get("ownerId").getAsLong());
        }catch (Exception ignore){}

        return board;
    }
}

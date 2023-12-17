package com.unipi.msc.raiseupandroid.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Long id;
    private String title;
    private Long date;
    private List<String> profiles = new ArrayList<>();
    private Long totalTasks;

    public Board() {
    }

    public Board(Long id, String title, Long date, List<String> profiles, Long totalTasks) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.profiles = profiles;
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

    public List<String> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<String> profiles) {
        this.profiles = profiles;
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
            JsonArray jsonArray = data.get("employees").getAsJsonArray();
            for (int i=0; i<jsonArray.size();i++) {
                JsonObject jsonObjectUser = jsonArray.get(i).getAsJsonObject();
                board.getProfiles().add(jsonObjectUser.get("profile").getAsString());
            }
        }catch (Exception ignore){}

        try {
            board.setTotalTasks(data.get("totalTasks").getAsLong());
        }catch (Exception ignore){}

        return board;
    }
}

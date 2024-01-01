package com.unipi.msc.riseupandroid.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Column {
    private Long id;
    private String title;
    private Long boardId;
    private List<Task> tasks = new ArrayList<>();

    public Column() {
    }

    public Column(Long id, String title) {
        this.id = id;
        this.title = title;
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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public static Column buildFromJSON(JsonObject jsonObject){
        Column column = new Column();

        try {
            column.setId(jsonObject.get("id").getAsLong());
        }catch (Exception ignore){}

        try {
            column.setTitle(jsonObject.get("title").getAsString());
        }catch (Exception ignore){}

        try {
            column.setBoardId(jsonObject.get("boardId").getAsLong());
        }catch (Exception ignore){}

        try {
            JsonArray jsonArray = jsonObject.get("tasks").getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                column.getTasks().add(Task.buildTaskFromJSON(jsonArray.get(i).getAsJsonObject()));
            }
        }catch (Exception ignore){}

        return column;
    }
}

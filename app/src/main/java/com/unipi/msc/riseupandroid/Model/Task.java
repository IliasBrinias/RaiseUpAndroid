package com.unipi.msc.riseupandroid.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private Long id;
    private String title;
    private String description;
    private Long dueDate;
    private Boolean completed;
    private Column column;
    private Difficulty difficulty;
    private List<User> users = new ArrayList<>();
    private List<Tag> tags = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();

    public Task() {}

    public Task(Long id, String title, String description, Long dueDate, Boolean completed, Column column, List<User> users, List<Tag> tags, List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = completed;
        this.column = column;
        this.users = users;
        this.tags = tags;
        this.comments = comments;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDueDate() {
        return dueDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public static Task buildTaskFromJSON(JsonObject jsonObject){
        Task task = new Task();

        try {
            task.setId(jsonObject.get("id").getAsLong());
        }catch (Exception ignore){}

        try {
            task.setTitle(jsonObject.get("title").getAsString());
        }catch (Exception ignore){}

        try {
            task.setDescription(jsonObject.get("description").getAsString());
        }catch (Exception ignore){}

        try {
            task.setDueDate(jsonObject.get("dueTo").getAsLong());
        }catch (Exception ignore){}

        try {
            task.setCompleted(jsonObject.get("completed").getAsBoolean());
        }catch (Exception ignore){}

        try {
            task.setDifficulty(Difficulty.valueOf(jsonObject.get("difficulty").getAsString()));
        }catch (Exception ignore){}

        try {
            task.setColumn(Column.buildFromJSON(jsonObject.get("step").getAsJsonObject()));
        }catch (Exception ignore){}

        try {
            JsonArray jsonArray = jsonObject.get("users").getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                task.getUsers().add(User.buildFromJSON(jsonArray.get(i).getAsJsonObject()));
            }
        }catch (Exception ignore){}

        try {
            JsonArray jsonArray = jsonObject.get("comments").getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                task.getComments().add(Comment.buildFromJSON(jsonArray.get(i).getAsJsonObject()));
            }
        }catch (Exception ignore){}

        try {
            JsonArray jsonArray = jsonObject.get("tags").getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                task.getTags().add(Tag.buildFromJSON(jsonArray.get(i).getAsJsonObject()));
            }
        }catch (Exception ignore){}

        return task;
    }
}

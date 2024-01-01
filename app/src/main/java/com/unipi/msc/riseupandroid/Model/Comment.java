package com.unipi.msc.riseupandroid.Model;

import com.google.gson.JsonObject;

public class Comment {
    private Long id;
    private String message;
    private Long date;

    public Comment() {
    }

    public Comment(Long id, String message, Long date) {
        this.id = id;
        this.message = message;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
    public static Comment buildFromJSON(JsonObject jsonObject){
        Comment comment = new Comment();

        try {
            comment.setId(jsonObject.get("id").getAsLong());
        }catch (Exception ignore){}

        try {
            comment.setMessage(jsonObject.get("message").getAsString());
        }catch (Exception ignore){}

        try {
            comment.setDate(jsonObject.get("date").getAsLong());
        }catch (Exception ignore){}

        return comment;
    }
}

package com.unipi.msc.riseupandroid.Model;

import com.google.gson.JsonObject;

public class Tag {
    private Long id;
    private String name;
    private String color;

    public Tag() {
    }

    public Tag(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
    public static Tag buildFromJSON(JsonObject body){
        Tag tag = new Tag();

        try {
            tag.setId(body.get("id").getAsLong());
        }catch (Exception ignore){}

        try {
            tag.setName(body.get("name").getAsString());
        }catch (Exception ignore){}

        try {
            tag.setColor(body.get("color").getAsString());
        }catch (Exception ignore){}

        return tag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

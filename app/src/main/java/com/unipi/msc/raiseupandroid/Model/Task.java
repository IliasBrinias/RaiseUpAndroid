package com.unipi.msc.raiseupandroid.Model;

import java.util.List;

public class Task {
    private Long id;
    private String name;
    private String dsc;
    private Long dueDate;
    private Column column;
    private List<User> users;
    private List<Tag> tags;

    public Task(Long id, String name, String dsc, Long dueDate, Column column, List<User> users, List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.dsc = dsc;
        this.dueDate = dueDate;
        this.column = column;
        this.users = users;
        this.tags = tags;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDsc() {
        return dsc;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc;
    }

    public Long getDueDate() {
        return dueDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }

    public List<User> getEmployees() {
        return users;
    }

    public void setEmployees(List<User> users) {
        this.users = users;
    }
}

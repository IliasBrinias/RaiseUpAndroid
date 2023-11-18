package com.unipi.msc.raiseupandroid.Model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Long id;
    private String title;
    private Long dueDate;
    private List<String> employees;
    private Long tasks;

    public Board(Long id, String title, Long dueDate, List<String> employees, Long tasks) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.employees = employees;
        this.tasks = tasks;
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

    public List<String> getEmployees() {
        return employees;
    }

    public void setEmployees(List<String> employees) {
        this.employees = employees;
    }

    public Long getTasks() {
        return tasks;
    }

    public void setTasks(Long tasks) {
        this.tasks = tasks;
    }

    public Long getDueDate() {
        return dueDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }
}

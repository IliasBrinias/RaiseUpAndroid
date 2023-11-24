package com.unipi.msc.raiseupandroid.Model;

import java.util.List;

public class Task {
    private Long id;
    private String name;
    private String dsc;
    private Long dueDate;
    private List<Employee> employees;
    private List<Tag> tags;

    public Task(Long id, String name, String dsc, Long dueDate, List<Employee> employees, List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.dsc = dsc;
        this.dueDate = dueDate;
        this.employees = employees;
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

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}

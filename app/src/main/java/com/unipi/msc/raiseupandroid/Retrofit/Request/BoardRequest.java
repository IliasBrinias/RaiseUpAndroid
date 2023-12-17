package com.unipi.msc.raiseupandroid.Retrofit.Request;

import java.util.ArrayList;
import java.util.List;

public class BoardRequest {
    private String title;
    private List<Long> employeesId = new ArrayList<>();
    private List<String> columns = new ArrayList<>();

    public BoardRequest(String title, List<Long> employeesId, List<String> columns) {
        this.title = title;
        this.employeesId = employeesId;
        this.columns = columns;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Long> getEmployeesId() {
        return employeesId;
    }

    public void setEmployeesId(List<Long> employeesId) {
        this.employeesId = employeesId;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
}

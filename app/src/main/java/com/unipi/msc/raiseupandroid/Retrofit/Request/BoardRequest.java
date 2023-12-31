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

    public BoardRequest(Builder builder) {
        this.title = builder.title;
        this.employeesId = builder.employeesId;
        this.columns = builder.columns;
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
    public static class Builder{
        private String title;
        private List<Long> employeesId = new ArrayList<>();
        private List<String> columns = new ArrayList<>();

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setEmployeesId(List<Long> employeesId) {
            this.employeesId = employeesId;
            return this;
        }

        public Builder setColumns(List<String> columns) {
            this.columns = columns;
            return this;
        }
        public BoardRequest build(){
            return new BoardRequest(this);
        }
    }
}
